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
package org.imboproject.javaclient;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.imboproject.javaclient.Http.ResponseInterface;
import org.imboproject.javaclient.Images.Image;
import org.imboproject.javaclient.Images.QueryInterface;
import org.imboproject.javaclient.Url.ImageUrl;
import org.imboproject.javaclient.Url.ImagesUrl;
import org.imboproject.javaclient.Url.MetadataUrl;
import org.imboproject.javaclient.Url.StatusUrl;
import org.imboproject.javaclient.Url.UrlInterface;
import org.imboproject.javaclient.Url.UserUrl;
import org.imboproject.javaclient.util.Crypto;
import org.imboproject.javaclient.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Imbo client
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class Client implements ClientInterface {

    /**
     * URLs for the server hosts
     */
    private String[] serverUrls;

    /**
     * Public key used for signed requests
     */
    private String publicKey;

    /**
     * Private key used for signed requests
     */
    private String privateKey;

    /**
     * Holds a HTTP client instance
     */
    private org.imboproject.javaclient.Http.ClientInterface httpClient;

    /**
     * Constructs the Imbo client
     *
     * @param serverUrl URL to the server
     * @param publicKey Public key to use for this instance
     * @param privateKey Private key to use for this instance
     */
    public Client(String serverUrl, String publicKey, String privateKey) {
        this.serverUrls = parseUrls(serverUrl);
        this.publicKey  = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * Constructs the Imbo client
     *
     * @param serverUrls URLs to the server
     * @param publicKey Public key to use for this instance
     * @param privateKey Private key to use for this instance
     */
    public Client(String[] serverUrls, String publicKey, String privateKey) {
        this.serverUrls = parseUrls(serverUrls);
        this.publicKey  = publicKey;
        this.privateKey = privateKey;
    }

    /**
     * {@inheritDoc}
     */
    public String[] getServerUrls() {
        return serverUrls;
    }

    /**
     * {@inheritDoc}
     */
    public StatusUrl getStatusUrl() {
        return new StatusUrl(serverUrls[0]);
    }

    /**
     * {@inheritDoc}
     */
    public UserUrl getUserUrl() {
        return new UserUrl(serverUrls[0], publicKey, privateKey);
    }

    /**
     * {@inheritDoc}
     */
    public ImagesUrl getImagesUrl() {
        return new ImagesUrl(serverUrls[0], publicKey, privateKey);
    }

    /**
     * {@inheritDoc}
     */
    public ImageUrl getImageUrl(String imageIdentifier) {
        String hostname = getHostForImageIdentifier(imageIdentifier);

        return new ImageUrl(hostname, publicKey, privateKey, imageIdentifier);
    }

    /**
     * {@inheritDoc}
     */
    public MetadataUrl getMetadataUrl(String imageIdentifier) {
        String hostname = getHostForImageIdentifier(imageIdentifier);

        return new MetadataUrl(hostname, publicKey, privateKey, imageIdentifier);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseInterface addImage(File image) throws IOException {
        String imageIdentifier = getImageIdentifier(image);
        URI signedUrl = getSignedUrl("PUT", getImageUrl(imageIdentifier));

        return httpClient.put(signedUrl, image);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseInterface addImage(byte[] bytes) throws IOException {
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("Byte array is empty");
        }

        String imageIdentifier = getImageIdentifier(bytes);
        URI signedUrl = getSignedUrl("PUT", getImageUrl(imageIdentifier));
        ByteArrayInputStream buffer = new ByteArrayInputStream(bytes);

        return httpClient.put(signedUrl, buffer);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseInterface addImageFromUrl(URI url) throws IOException {
        ResponseInterface response = httpClient.get(url);

        return addImage(response.getRawBody());
    }

    /**
     * {@inheritDoc}
     */
    public boolean imageExists(File image) throws IllegalArgumentException, IOException {
        validateLocalFile(image);
        
        String imageIdentifier = getImageIdentifier(image);
        
        return this.imageExists(imageIdentifier);
    }

    /**
     * {@inheritDoc}
     */
    public boolean imageExists(String imageIdentifier) throws IOException {
        try {
            this.headImage(imageIdentifier);
        } catch (ServerException e) {
            if (e.getErrorCode() == 404) {
                return false;
            }
            
            throw e;
        }
        
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public ResponseInterface headImage(String imageIdentifier) throws IOException {
        ImageUrl url = this.getImageUrl(imageIdentifier);
        
        return this.httpClient.head(url);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseInterface deleteImage(String imageIdentifier) throws IOException {
        ImageUrl url  = this.getImageUrl(imageIdentifier);
        URI signedUrl = this.getSignedUrl("DELETE", url);
        
        return this.httpClient.delete(signedUrl);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseInterface editMetadata(String imageIdentifier, JSONObject metadata) throws IOException {
        MetadataUrl url = this.getMetadataUrl(imageIdentifier);
        URI signedUrl = this.getSignedUrl(org.imboproject.javaclient.Http.ClientInterface.POST, url);

        String data = metadata.toString();
        
        Header[] headers = new Header[] {
            new BasicHeader("Content-Type", "application/json"),
            new BasicHeader("Content-Length", data.length() + ""),
            new BasicHeader("Content-MD5", "@todo")
        };
        
        return this.httpClient.post(signedUrl, data, headers);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseInterface replaceMetadata(String imageIdentifier, JSONObject metadata) throws IOException {
        MetadataUrl url = this.getMetadataUrl(imageIdentifier);
        URI signedUrl = this.getSignedUrl(org.imboproject.javaclient.Http.ClientInterface.PUT, url);

        String data = metadata.toString();
        
        Header[] headers = new Header[] {
            new BasicHeader("Content-Type", "application/json"),
            new BasicHeader("Content-Length", data.length() + ""),
            new BasicHeader("Content-MD5", "@todo")
        };
        
        return this.httpClient.put(signedUrl, data, headers);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseInterface deleteMetadata(String imageIdentifier) throws IOException {
        MetadataUrl url = this.getMetadataUrl(imageIdentifier);
        URI signedUrl = this.getSignedUrl(org.imboproject.javaclient.Http.ClientInterface.DELETE, url);

        return this.httpClient.delete(signedUrl);
    }

    /**
     * {@inheritDoc}
     */
    public JSONObject getMetadata(String imageIdentifier) throws JSONException, IOException {
        MetadataUrl url = this.getMetadataUrl(imageIdentifier);
        
        ResponseInterface response = this.httpClient.get(url);

        JSONObject body = new JSONObject(response.getBody());

        return body;
    }

    /**
     * {@inheritDoc}
     */
    public int getNumberOfImages() throws IOException, JSONException {
        UserUrl url = this.getUserUrl();
        ResponseInterface response = this.httpClient.get(url);
        
        JSONObject body = new JSONObject(response.getBody());

        return body.optInt("numImages", 42);
    }

    /**
     * {@inheritDoc} 
     */
    public org.imboproject.javaclient.Images.Image[] getImages() throws IOException, JSONException {
        return this.getImages(null);
    }

    /**
     * {@inheritDoc}
     * @throws IOException 
     * @throws JSONException 
     */
    public org.imboproject.javaclient.Images.Image[] getImages(QueryInterface query) throws IOException, JSONException {
        ImagesUrl url = this.getImagesUrl();
        HashMap<String, String> params = null;
        
        if (query != null) {
            params = query.toHashMap();
            
            String key;
            Iterator<String> keyIterator = params.keySet().iterator();
            while (keyIterator.hasNext()) {
                key = keyIterator.next();
                url.addQueryParam(key, params.get(key));
            }
        }
        
        // Fetch the response
        ResponseInterface response = this.httpClient.get(url.toUri());
        JSONArray images = new JSONArray(response.getBody());
        
        LinkedList<Image> instances = new LinkedList<Image>();
        for (int i = 0; i < images.length(); i++) {
            instances.add(new Image(images.getJSONObject(i)));
        }
        
        return instances.toArray(new Image[instances.size()]);
    }

    /**
     * {@inheritDoc}
     */
    public byte[] getImageData(String imageIdentifier) throws IOException {
        return this.getImageData(this.getImageUrl(imageIdentifier).toUri());
    }

    /**
     * {@inheritDoc}
     */
    public byte[] getImageData(URI url) throws IOException {
        ResponseInterface response = this.httpClient.get(url);
        
        return response.getRawBody();
    }

    /**
     * {@inheritDoc}
     */
    public Image getImageProperties(String imageIdentifier) throws IOException {
        ResponseInterface response = this.headImage(imageIdentifier);
        HashMap<String, String> headers = response.getHeaders();

        JSONObject data = new JSONObject();
        try {
            data.put("imageIdentifier", imageIdentifier);
            data.put("extension",       headers.get("x-imbo-originalextension"));
            data.put("mime",            headers.get("x-imbo-originalmimetype"));
            data.put("size",            Integer.parseInt(headers.get("x-imbo-originalfilesize")));
            data.put("width",           Integer.parseInt(headers.get("x-imbo-originalwidth")));
            data.put("height",          Integer.parseInt(headers.get("x-imbo-originalheight")));
            
        } catch (NumberFormatException e) {
            
        } catch (JSONException e) {
            
        }
        
        return new Image(data);
    }

    /**
     * {@inheritDoc}
     */
    public String getImageIdentifier(File file) throws IOException {
        validateLocalFile(file);
    	InputStream is = new FileInputStream(file);

        return getImageIdentifier(is);
    }

    /**
     * {@inheritDoc}
     */
    public String getImageIdentifier(InputStream imageStream) throws IOException {
        MessageDigest complete = null;
        try {
             complete = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "md5-algo-not-defined";
        }

        byte[] buffer = new byte[1024];
        int numRead;

        do {
            numRead = imageStream.read(buffer);
            if (numRead > 0) {
                complete.update(buffer, 0, numRead);
            }
        } while (numRead != -1);

        imageStream.close();

        byte[] bytes = complete.digest();
        String result = "";

        for (int i = 0; i < bytes.length; i++) {
            result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public String getImageIdentifier(byte[] imageData) {
        MessageDigest complete = null;
        try {
             complete = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "md5-algo-not-defined";
        }

        byte[] bytes = complete.digest(imageData);
        String result = "";

        for (int i = 0; i < bytes.length; i++) {
            result += Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1);
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    public JSONObject getServerStatus() throws JSONException, IOException {

    	StatusUrl url = this.getStatusUrl();
    	ResponseInterface response;

        try {
           response = this.httpClient.get(url);
        } catch (ServerException e) {
            if (e.getErrorCode() == 500) {
                response = e.getResponse();
            } else {
                // Re-throw same exception
                throw e;
            }
        }

        return new JSONObject(response.getBody());
    }
    
    /**
     * {@inheritDoc} 
     */
    public JSONObject getUserInfo() throws JSONException, IOException {
    	UserUrl url = this.getUserUrl();
    	
    	ResponseInterface response = this.httpClient.get(url);
    	
        return new JSONObject(response.getBody());
    }

    /**
     * {@inheritDoc}
     */
    public ClientInterface setHttpClient(org.imboproject.javaclient.Http.ClientInterface client) {
        this.httpClient = client;

        return this;
    }

    /**
     * Generate a signature that can be sent to the server
     *
     * @param method HTTP method (PUT, POST or DELETE)
     * @param url The URL to send a request to
     * @param timestamp UTC time stamp
     * @return string
     */
    private String generateSignature(String method, String url, String timestamp) {
        String data = method + "|" + url + "|" + publicKey + "|" + timestamp;

        return Crypto.hashHmacSha256(data, this.privateKey);
    }

    /**
     * Get a signed URL
     * 
     * @param method HTTP method
     * @param url The URL to send a request to
     * @return Returns a URI with the necessary parts for authenticating
     */
    private URI getSignedUrl(String method, UrlInterface url) {
        return getSignedUrl(method, url.toString());
    }

    /**
     * Get a signed URL
     *
     * @param method HTTP method
     * @param url The URL to send a request to
     * @return Returns a string with the necessary parts for authenticating
     */
    private URI getSignedUrl(String method, String url) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String timestamp = df.format(new Date());
        String signature = generateSignature(method, url, timestamp);

        String[] parts = {
            url,
            (url.contains("?") ? "&" : "?"),
            "signature=",
            TextUtils.urlEncode(signature),
            "&timestamp=",
            TextUtils.urlEncode(timestamp)
        };

        URI signed;
        try {
            signed = new URI(TextUtils.join("", parts));
        } catch (URISyntaxException e) {
            return null;
        }

        return signed;
    }

    /**
     * Parse server host URLs and prepare them for usage
     *
     * @param host URL for the Imbo server
     * @return Array of URLs
     */
    private String[] parseUrls(String host) {
        String[] urls = { host };
        return parseUrls(urls);
    }

    /**
     * Parse server host URLs and prepare them for usage
     *
     * @param hosts URLs for the Imbo server
     * @return Array of URLs
     */
    private String[] parseUrls(String[] hosts) {
        LinkedList<String> urls = new LinkedList<String>();

        for (String host : hosts) {
            host = normalizeUrl(host);

            if (host != null && !urls.contains(host)) {
                urls.add(host);
            }
        }

        return urls.toArray(new String[urls.size()]);
    }

    /**
     * Normalize a URL
     *
     * @param url Input URL
     * @return Normalized URL
     */
    private String normalizeUrl(String url) {
        URI parsedUrl;
        
        if (!url.matches("^https?://.*")) {
            url = "http://" + url;
        }

        try {
            parsedUrl = new URI(url);
        } catch (URISyntaxException e) {
            return null;
        }
        
        // Remove the port from the server URL if it's equal to 80 when scheme is http, or if
        // it's equal to 443 when the scheme is https
        if (parsedUrl.getPort() != -1 && (
                (parsedUrl.getScheme().equals("http")  && parsedUrl.getPort() == 80) ||
                (parsedUrl.getScheme().equals("https") && parsedUrl.getPort() == 443)
            )) {
            String path = parsedUrl.getPath();

            url = parsedUrl.getScheme() + "://" + parsedUrl.getHost() + path;
        }

        if (url.endsWith("/")) {
            url = url.substring(0, url.length() - 1);
        }

        return url;
    }

    /**
     * Get a predictable hostname for the given image identifier
     *
     * @param imageIdentifier Image identifier to get host for
     * @return Hostname in string format
     */
    private String getHostForImageIdentifier(String imageIdentifier) {
        int dec = Integer.parseInt(imageIdentifier.substring(0, 2), 16);

        return serverUrls[dec % serverUrls.length];
    }

    /**
     * Validate a local file
     *
     * @param file File to validate
     * @throws IllegalArgumentException
     * @throws FileNotFoundException
     */
    private void validateLocalFile(File file) throws IllegalArgumentException, FileNotFoundException {
        if (!file.exists()) {
            throw new FileNotFoundException("The system cannot find the file specified");
        }

        if (file.length() == 0) {
            throw new IllegalArgumentException("The specified file was empty");
        }
    }

}
