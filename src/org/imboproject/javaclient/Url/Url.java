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
 * Abstract Imbo URL for other implementations to extend
 * 
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
abstract class Url implements UrlInterface {
    
    /**
     * Base URL
     */
    protected String baseUrl;
    
    /**
     * Public key
     */
    protected String publicKey;
    
    /**
     * Private key
     */
    private String privateKey;
    
    /**
     * Access token generator
     */
    private AccessTokenInterface accessToken; 
    
    /**
     * Query parameters for the URL
     */
    private ArrayList<String> queryParams = new ArrayList<String>();
    
    /**
     * Class constructor
     * 
     * @param baseUrl The base URL to use
     * @param publicKey The public key to use
     * @param privateKey The private key to use
     */
    public Url(String baseUrl, String publicKey, String privateKey) {
        this.baseUrl = baseUrl;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrl() {
        String url = getResourceUrl();
        String queryString = getQueryString();
        
        if (!queryString.isEmpty()) {
            url += "?" + queryString;
        }
        
        if (publicKey == null || privateKey == null) {
            return url;
        }
        
        String token = getAccessToken().generateToken(url, privateKey);
        
        return url + (queryParams.isEmpty() ? "?" : "&") + "accessToken=" + token;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrlEncoded() {
        String url = getUrl();
        url = url.replace("&", "&amp;");
        url = url.replace("[]", "%5B%5D");
        return url;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getUrl();
    }

    @Override
    public UrlInterface addQueryParam(String key, String value) {
        queryParams.add(key + "=" + TextUtils.urlEncode(value));
        
        return this;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UrlInterface reset() {
        queryParams.clear();
        return this;
    }
    
    /**
     * Get an instance of the access token
     *
     * If no instance have been provided prior to calling this method, this method must
     * instantiate the AccessToken class and return that instance.
     *
     * @return AccessTokenInterface
     */
    public AccessTokenInterface getAccessToken() {
        if (accessToken == null) {
            accessToken = new AccessToken();
        }
        
        return accessToken;
    }
    
    /**
     * Set an instance of the access token
     *
     * @return AccessTokenInterface $accessToken An instance of the access token
     * @return UrlInterface
     */
    public UrlInterface setAccessToken(AccessTokenInterface accessToken) {
        this.accessToken = accessToken;
        return this;
    }
    
    private String getQueryString() {
        if (queryParams.isEmpty()) {
            return "";
        }
        
        return TextUtils.join("&", queryParams);
    }
    
    /**
     * Get the raw URL (with no access token appended)
     * 
     * @return The raw URL as a String
     */
    abstract protected String getResourceUrl();
}