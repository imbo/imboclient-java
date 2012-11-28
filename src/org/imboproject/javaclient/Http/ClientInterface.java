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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.http.Header;

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
     * @throws IOException
     * @return HTTP response
     */
    public Response post(URI url, HashMap<String, String> data) throws IOException;

    /**
     * Perform a GET-request against the given URL
     *
     * @param url URL to perform request against
     * @throws IOException
     * @return HTTP response
     */
    public Response get(URI url) throws IOException;

    /**
     * Perform a HEAD-request against the given URL
     *
     * @param url URL to perform request against
     * @throws IOException
     * @return HTTP response
     */
    public Response head(URI url) throws IOException;

    /**
     * Perform a DELETE-request against the given URL
     *
     * @param url URL to perform request against
     * @throws IOException
     * @return HTTP response
     */
    public Response delete(URI url) throws IOException;

    /**
     * Perform a PUT-request against the given URL
     *
     * @param url URL to perform request against
     * @param data Raw data to PUT, as String
     * @throws IOException
     * @return HTTP response
     */
    public Response put(URI url, String data) throws IOException;

    /**
     * Perform a PUT-request against the given URL
     *
     * @param url URL to perform request against
     * @param input Input stream to use for reading PUT-data from
     * @throws IOException
     * @return HTTP response
     */
    public Response put(URI url, InputStream input) throws IOException;

    /**
     * Set request headers to send along with the request
     *
     * @param headers Linked list of headers to send
     * @return This client instance
     */
    public ClientInterface setRequestHeaders(LinkedList<Header> headers);

}
