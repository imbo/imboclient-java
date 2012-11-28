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

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

/**
 * HTTP Client interface
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public interface ClientInterface {

    /**
     * Perform a POST-request against the given URL
     *
     * @param url URL to perform request against
     * @param data Post-data to send
     * @return HTTP response
     */
    public Response post(URL url, HashMap<String, String> data);

    /**
     * Perform a GET-request against the given URL
     *
     * @param url URL to perform request against
     * @return HTTP response
     */
    public Response get(URL url);

    /**
     * Perform a HEAD-request against the given URL
     *
     * @param url URL to perform request against
     * @return HTTP response
     */
    public Response head(URL url);

    /**
     * Perform a DELETE-request against the given URL
     *
     * @param url URL to perform request against
     * @return HTTP response
     */
    public Response delete(URL url);

    /**
     * Perform a PUT-request against the given URL
     *
     * @param url URL to perform request against
     * @param data Raw data to PUT, as String
     * @return HTTP response
     */
    public Response put(URL url, String data);

    /**
     * Perform a PUT-request against the given URL
     *
     * @param url URL to perform request against
     * @param input Input stream to use for reading PUT-data from
     * @return HTTP response
     */
    public Response put(URL url, InputStream input);

    /**
     * Set request headers to send along with the request
     *
     * @param headers Hash map of header names and values to send
     * @return This client instance
     */
    public ClientInterface setRequestHeaders(HashMap<String, String> headers);

}
