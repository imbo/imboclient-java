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
import java.util.LinkedList;

import org.imboproject.javaclient.Http.ResponseInterface;
import org.imboproject.javaclient.Images.ImageInterface;
import org.imboproject.javaclient.Images.QueryInterface;
import org.imboproject.javaclient.Url.Image;
import org.imboproject.javaclient.Url.Images;
import org.imboproject.javaclient.Url.Metadata;
import org.imboproject.javaclient.Url.Status;
import org.imboproject.javaclient.Url.User;
import org.imboproject.javaclient.util.Crypto;
import org.imboproject.javaclient.util.TextUtils;
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
    public Status getStatusUrl() {
        return new Status(serverUrls[0], null, null);
    }

    /**
     * {@inheritDoc}
     */
    public User getUserUrl() {
        return new User(serverUrls[0], publicKey, privateKey);
    }

    /**
     * {@inheritDoc}
     */
    public Images getImagesUrl() {
        return new Images(serverUrls[0], publicKey, privateKey);
    }

    /**
     * {@inheritDoc}
     */
    public Image getImageUrl(String imageIdentifier) {
        String hostname = getHostForImageIdentifier(imageIdentifier);

        return new Image(hostname, publicKey, privateKey, imageIdentifier);
    }

    /**
     * {@inheritDoc}
     */
    public Metadata getMetadataUrl(String imageIdentifier) {
        String hostname = getHostForImageIdentifier(imageIdentifier);

        return new Metadata(hostname, publicKey, privateKey, imageIdentifier);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseInterface addImage(File image) throws IOException {
        validateLocalFile(image);

        String imageIdentifier = getImageIdentifier(image);

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public ResponseInterface addImageFromUrl(URI url) {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean imageExists(File image) {

        return false;
    }

    /**
     * {@inheritDoc}
     */
    public boolean imageExists(String imageIdentifier) {

        return false;
    }

    /**
     * {@inheritDoc}
     */
    public ResponseInterface headImage(String imageIdentifier) {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public ResponseInterface deleteImage(String imageIdentifier) {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public ResponseInterface editMetadata(String imageIdentifier, JSONObject metadata) {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public ResponseInterface replaceMetadata(String imageIdentifier, JSONObject metadata) {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public ResponseInterface deleteMetadata(String imageIdentifier) {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public JSONObject getMetadata(String imageIdentifier) {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public int getNumberOfImages() {

        return 0;
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface[] getImages() {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public ImageInterface[] getImages(QueryInterface query) {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public byte[] getImageData(String imageIdentifier) {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public byte[] getImageData(URI url) {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Image getImageProperties(String imageIdentifier) {

        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getImageIdentifier(File file) throws IOException {
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
    public JSONObject getServerStatus() {

        return null;
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

        return Crypto.hashHmacSha256(data, privateKey);
    }

    /**
     * Get a signed URL
     *
     * @param method HTTP method
     * @param url The URL to send a request to
     * @return Returns a string with the necessary parts for authenticating
     */
    private String getSignedUrl(String method, String url) {
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

        return TextUtils.join("", parts);
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

            if (host == null || !urls.contains(host)) {
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

        if (!url.matches("^https?://")) {
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
                (parsedUrl.getScheme() == "http"  && parsedUrl.getPort() == 80) ||
                (parsedUrl.getScheme() == "https" && parsedUrl.getPort() == 443)
            )) {
            String path = parsedUrl.getPath();
            url= parsedUrl.getScheme() + "://" + parsedUrl.getHost() + (path == null ? "" : path);
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
