/**
 * ImboClient-java
 *
 * Copyright (c) 2012, Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * * The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 * @copyright Copyright (c) 2012, Espen Hovlandsdal <espen@hovlandsdal.com>
 * @license http://www.opensource.org/licenses/mit-license MIT License
 * @link https://github.com/rexxars/imboclient-java
 */
package org.imboproject.javaclient.Url;

import java.util.ArrayList;

import org.imboproject.javaclient.util.TextUtils;

/**
 * Image URL
 * 
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class ImageUrl extends Url implements ImageInterface {

    /**
     * Image identifier for this image
     */
    private String imageIdentifier;
    
    /**
     * Class constructor
     * 
     * @param baseUrl The base URL to use
     * @param publicKey The public key to use
     * @param privateKey The private key to use
     * @param imageIdentifier The image identifier to use in the URL
     */
    public ImageUrl(String baseUrl, String publicKey, String privateKey, String imageIdentifier) {
        super(baseUrl, publicKey, privateKey);
        
        this.imageIdentifier = imageIdentifier;
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface border(String color, int width, int height) {
        if (color == null) {
            color = "000000";
        }
        
        addQueryParam("t[]", ( 
                "border:color=" +
                TextUtils.normalizeColor(color) +
                ",width=" + width +
                ",height=" + height
            )
        );
        
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    public ImageInterface border(String color) {
        return border(color, 1, 1);
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface compress(int quality) {
        addQueryParam("t[]", "compress:quality=" + quality);
        
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    public ImageInterface compress() {
        return compress(75);
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface convert(String type) {
        imageIdentifier = imageIdentifier.substring(0, 32) + "." + type;
        
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface gif() {
        return convert("gif");
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface jpg() {
        return convert("jpg");
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface png() {
        return convert("png");
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface crop(int x, int y, int width, int height) {
        addQueryParam("t[]", ( 
                "crop:" +
                "x=" + x +
                ",y=" + y + 
                ",width=" + width +
                ",height=" + height
            )
        );
        
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface flipHorizontally() {
        addQueryParam("t[]", "flipHorizontally");
        
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface flipVertically() {
        addQueryParam("t[]", "flipVertically");
        
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface resize(int width, int height) {
        ArrayList<String> params = new ArrayList<String>();
        
        if (width > 0) {
            params.add("width=" + width);
        }
        
        if (height > 0) {
            params.add("height=" + height);
        }
        
        addQueryParam("t[]", "resize:" + TextUtils.join(",", params));
        
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface maxSize(int maxWidth, int maxHeight) {
        ArrayList<String> params = new ArrayList<String>();
        
        if (maxWidth > 0) {
            params.add("width=" + maxWidth);
        }
        
        if (maxHeight > 0) {
            params.add("height=" + maxHeight);
        }
        
        addQueryParam("t[]", "maxSize:" + TextUtils.join(",", params));
        
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface rotate(double angle, String bg) {
        // Don't put decimals into the URL if we have no fractions
        String ang = Double.toString(angle);
        if ((angle - (int) angle) == 0) {
            ang = Integer.toString((int) angle);
        }
        
        addQueryParam("t[]",
            "rotate:angle=" + ang +
            ",bg=" + TextUtils.normalizeColor(bg)
        );
        
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    public ImageInterface rotate(double angle) {
        return rotate(angle, "000000");
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface thumbnail(int width, int height, String fit) {
        if (width == 0) {
            width = 50;
        }
        
        if (height == 0) {
            width = 50;
        }
        
        if (fit == null) {
            fit = "outbound";
        }
        
        addQueryParam("t[]", "thumbnail:width=" + width + ",height=" + height + ",fit=" + fit);
        
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    public ImageInterface thumbnail() {
        return thumbnail(50, 50, null);
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface canvas(int width, int height, String mode, int x, int y, String bg) {
        ArrayList<String> params = new ArrayList<String>();
        params.add("width="  + width);
        params.add("height=" + height);
        
        if (mode != null) {
            params.add("mode=" + mode);
        }
        
        params.add("x=" + x);
        params.add("y=" + y);
        
        if (bg != null) {
            params.add("bg=" + TextUtils.normalizeColor(bg));
        }
        
        addQueryParam("t[]", "canvas:" + TextUtils.join(",", params));
        
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    public ImageInterface canvas(int width, int height) {
        addQueryParam("t[]", "canvas:width=" + width + ",height=" + height);
        
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface transpose() {
        addQueryParam("t[]", "transpose");
        
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface transverse() {
        addQueryParam("t[]", "transverse");
        
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface desaturate() {
        addQueryParam("t[]", "desaturate");
        
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UrlInterface reset() {
        super.reset();
        
        imageIdentifier = imageIdentifier.substring(0, 32);
        
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    protected String getResourceUrl() {
        String[] parts = {
            baseUrl,
            "users",
            publicKey,
            "images",
            imageIdentifier
        };
        return TextUtils.join("/", parts);
    }

}