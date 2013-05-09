/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package org.imboproject.javaclient.Url;

import java.net.URI;
import java.net.URISyntaxException;
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
    public String getUrlEncoded() {
        String url = getUrl();
        url = url.replace("&", "&amp;");
        url = url.replace("[]", "%5B%5D");
        return url;
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return getUrl();
    }

    /**
     * {@inheritDoc}
     */
    public URI toUri() {
        try {
            return new URI(getUrl());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        	return null;
        }
    }

    /**
     * {@inheritDoc}
     */
    public UrlInterface addQueryParam(String key, String value) {
        queryParams.add(key + "=" + TextUtils.urlEncode(value));

        return this;
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * Retrieves the query string for this URL
     *
     * @return Query string, as a String
     */
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