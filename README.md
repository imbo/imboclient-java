[![Build Status][1]][2]

# Java client for Imbo
A Java client for [Imbo](https://github.com/imbo/imbo).

## Usage

### Add an image
```java
ImboClient client = new ImboClient("http://<hostname>", "<publicKey>", "<privateKey>");
File imageFile = new File("/path/to/image.jpg");

try {
    Response response = client.addImage(imageFile);
    if (response.isSuccess()) {
        System.out.println("Image added! Image identifier: " + response.getImageIdentifier());
    }
} catch (ServerException err) {
    System.out.println("Oh no, an error occured! " + err.getMessage());
}
```

### Add/edit meta data
```java
ImboClient client = new ImboClient("http://<hostname>", "<publicKey>", "<privateKey>");
String imageIdentifier = "61da9892205a0d5077a353eb3487e8c8";

// Add some meta data to the image
JSONObject metadata = new JSONObject();
metadata.put("foo", "bar");
metadata.put("time", System.currentTimeMillis());

client.replaceMetadata(imageIdentifier, metadata);
```

### Get meta data
```java
ImboClient client = new ImboClient("http://<hostname>", "<publicKey>", "<privateKey>");
String imageIdentifier = "61da9892205a0d5077a353eb3487e8c8";

JSONObject metadata = client.getMetadata(imageIdentifier);
```

### Delete an image
```java
ImboClient client = new ImboClient("http://<hostname>", "<publicKey>", "<privateKey>");
String imageIdentifier = "61da9892205a0d5077a353eb3487e8c8";

client.deleteImage(imageIdentifier);
```

### Check if an image identifier exists on server
```java
ImboClient client = new ImboClient("http://<hostname>", "<publicKey>", "<privateKey>");
String imageIdentifier = "61da9892205a0d5077a353eb3487e8c8";

boolean exists = client.imageIdentifierExists(imageIdentifier);
```

### Generate image URLs

`ImageUrl` implements some methods that can be used to easily add transformations to an image URL. All these methods can be chained and the transformations will be applied to the URL in the chained order.

The `convert()` method is special in that it does not append anything to the URL, except injects an image extension to the image identifier. `convert()` (and `gif()`, `jpg()` and `png()` which proxies to `convert()`) can therefore be added anywhere in the chain.

Here's an example of how we can use it to resize an image while maintaining aspect ratio, then adding a border and outputting it in PNG format:

```java
ImboClient client = new ImboClient("http://<hostname>", "<publicKey>", "<privateKey>");
String imageIdentifier = "61da9892205a0d5077a353eb3487e8c8";

// Generate an image URL and add some transformations
ImageUrl imageUrl = client.getImageUrl(imageIdentifier).maxSize(320, 240).border("f00b", 2, 2).png();

// Print transformed URL
System.out.println(imageUrl.toString());
```

The transformations that can be chained are:

**border()**

Add a border around the image.

* `(String) color` Color in hexadecimal. Defaults to "000000" (also supports short values like "f00" ("ff0000")).
* `(int) width` Width of the border on the left and right sides of the image. Defaults to 1.
* `(int) height` Height of the border on the top and bottom sides of the image. Defaults to 1.

**canvas()**

Builds a new canvas and allows easy positioning of the original image within it.

* `(int) width` Width of the new canvas.
* `(int) height` Height of the new canvas.
* `(String) mode` Placement mode. "free" (uses `x` and `y`), "center", "center-x" (centers horizontally, uses `y` for vertical placement), "center-y" (centers vertically, uses `x` for horizontal placement). Default to "free".
* `(int) x` X coordinate of the placement of the upper left corner of the existing image.
* `(int) y` Y coordinate of the placement of the upper left corner of the existing image.
* `(String) bg` Background color of the canvas.

**compress()**

Compress the image on the fly.

* `(int) level` level of the resulting image. 100 is maximum quality (lowest compression rate)

**convert()**

Converts the image to another type.

* `(String) type` The type to convert to. Supported types are: "gif", "jpg" and "png".

**crop()**

Crop the image.

* `(int) x` The X coordinate of the cropped region's top left corner.
* `(int) y` The Y coordinate of the cropped region's top left corner.
* `(int) width` The width of the crop.
* `(int) height` The height of the crop.

**desaturate()**

Desaturates the image (essentially grayscales it).

**flipHorizontally()**

Flip the image horizontally.

**flipVertically()**

Flip the image vertically.

**gif()**

Proxies to `convert("gif")`.

**jpg()**

Proxies to `convert("jpg")`.

**maxSize()**

Resize the image using the original aspect ratio.

* `(int) width` The max width of the resulting image in pixels. If not specified the width will be calculated using the same ratio as the original image.
* `(int) height` The max height of the resulting image in pixels. If not specified the height will be calculated using the same ratio as the original image.

**png()**

Proxies to `convert("png")`.

**resize()**

Resize the image. Two parameters are supported and at least one of them must be supplied to apply this transformation.

* `(int) width` The width of the resulting image in pixels. If not specified the width will be calculated using the same ratio as the original image.
* `(int) height` The height of the resulting image in pixels. If not specified the height will be calculated using the same ratio as the original image.

**rotate()**

Rotate the image.

* `(int) angle` The number of degrees to rotate the image.
* `(String) bg` Background color in hexadecimal. Defaults to "000000" (also supports short values like "f00" ("ff0000")).

**sepia()**

Apply a sepia color tone transformation to the image.

* `(int) threshold` Measure of the extent of the sepia toning (ranges from 0 to QuantumRange). Defaults to 80.

**thumbnail()**

Generate a thumbnail of the image.

* `(int) width` Width of the thumbnail. Defaults to 50.
* `(int) height` Height of the thumbnail. Defaults to 50.
* `(String) fit` Fit style. "inset" or "outbound". Default to "outbound".

**transpose()**

Creates a vertical mirror image by reflecting the pixels around the central x-axis while rotating them 90-degrees.

**transverse()**

Creates a horizontal mirror image by reflecting the pixels around the central y-axis while rotating them 270-degrees.

### Support for multiple hostnames
Following the recommendation of the HTTP 1.1 specification, browsers typically default to two simultaneous requests per hostname. If you wish to generate URLs that point to a range of different hostnames, you can do this by passing an array of URLs to the client when instantiating:

```java
String[] hosts = new String[] {
    "http://<hostname1>",
    "http://<hostname2>",
    "http://<hostname3>"
};

ImboClient client = new ImboClient(hosts, "<publicKey>", "<privateKey>");
```

When using `getImageUrl(imageIdentifier)`, the client will pick one of the URLs defined. The same image identifier will result in the same URL, as long as the number of URLs given does not change.

## More examples
Check out the unit/integration tests for more examples and usage descriptions.

## License
Copyright (c) 2011-2014, Espen Hovlandsdal <espen@hovlandsdal.com>

Licensed under the MIT License

[1]: https://travis-ci.org/imbo/imboclient-java.png
[2]: https://travis-ci.org/imbo/imboclient-java
