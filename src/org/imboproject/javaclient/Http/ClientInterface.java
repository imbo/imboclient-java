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
package org.imboproject.javaclient.Http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.Header;
import org.imboproject.javaclient.Url.UrlInterface;

/**
 * HTTP Client interface
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public interface ClientInterface {

	/**
	 * HTTP methods
	 */
	public static final String GET = "GET";
	public static final String PUT = "PUT";
	public static final String HEAD = "HEAD";
	public static final String POST = "POST";
	public static final String DELETE = "DELETE";
	
	/**
     * Perform a POST-request against the given URL
     *
     * @param url URL to perform request against
     * @param data Post-data to send
     * @return HTTP response
     * @throws IOException
     */
    public ResponseInterface post(URI url, String data) throws IOException;
	
    /**
     * Perform a POST-request against the given URL
     *
     * @param url URL to perform request against
     * @param data Post-data to send
     * @param headers Headers to send along with the request
     * @return HTTP response
     * @throws IOException
     */
    public ResponseInterface post(URI url, String data, Header[] headers) throws IOException;

    /**
     * Perform a GET-request against the given URL
     *
     * @param url URL to perform request against
     * @return HTTP response
     * @throws IOException
     */
    public ResponseInterface get(URI url) throws IOException;
    
    /**
     * Perform a GET-request against the given URL
     *
     * @param url URL to perform request against
     * @return HTTP response
     * @throws IOException
     */
    public ResponseInterface get(UrlInterface url) throws IOException;

    /**
     * Perform a HEAD-request against the given URL
     *
     * @param url URL to perform request against
     * @return HTTP response
     * @throws IOException
     */
    public ResponseInterface head(URI url) throws IOException;
    
    /**
     * Perform a HEAD-request against the given URL
     *
     * @param url URL to perform request against
     * @return HTTP response
     * @throws IOException
     */
    public ResponseInterface head(UrlInterface url) throws IOException;

    /**
     * Perform a DELETE-request against the given URL
     *
     * @param url URL to perform request against
     * @return HTTP response
     * @throws IOException
     */
    public ResponseInterface delete(URI url) throws IOException;

    /**
     * Perform a PUT-request against the given URL
     *
     * @param url URL to perform request against
     * @param data Raw data to PUT, as String
     * @param headers Headers to send along with the request
     * @return HTTP response
     * @throws IOException
     */
    public ResponseInterface put(URI url, String data, Header[] headers) throws IOException;

    /**
     * Perform a PUT-request against the given URL
     *
     * @param url URL to perform request against
     * @param input Input stream to use for reading PUT-data from
     * @return HTTP response
     * @throws IOException
     */
    public ResponseInterface put(URI url, InputStream input) throws IOException;

    /**
     * Perform a PUT-request against the given URL
     *
     * @param url URL to perform request against
     * @param file File to send along with request
     * @return HTTp response
     * @throws IOException
     */
    public ResponseInterface put(URI url, File file) throws IOException;

    /**
     * Set request headers to send along with the request
     *
     * @param headers Headers to send
     * @return This client instance
     */
    public ClientInterface setRequestHeaders(Header[] headers);

}
