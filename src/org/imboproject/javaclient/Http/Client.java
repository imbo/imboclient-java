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
 * Imbo HTTP client
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class Client implements ClientInterface {

    /**
     * {@inheritDoc}
     */
    public Response post(URL url, HashMap<String, String> data) {
        return request("POST", url, data);
    }

    /**
     * {@inheritDoc}
     */
    public Response get(URL url) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Response head(URL url) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Response delete(URL url) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Response put(URL url, String data) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Response put(URL url, InputStream input) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Client setRequestHeaders(HashMap<String, String> headers) {
        return this;
    }

    /**
     * Perform a request of the given HTTP method against the given URL
     *
     * @param method HTTP method to use
     * @param url URL to request
     * @param headers Headers to send along with the request
     * @return HTTP response
     */
    protected Response request(String method, URL url, HashMap<String, String> headers) {
        return null;
    }

}
