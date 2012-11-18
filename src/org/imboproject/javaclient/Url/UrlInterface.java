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

/**
 * URL interface
 * 
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public interface UrlInterface {

    /**
     * Returns the URL with query parameters added
     * 
     * @return Full URL with query parameters, as a String
     */
    public String getUrl();
    
    /**
     * Get the complete URL as an URL-encoded string
     * 
     * @return URL-encoded string
     */
    public String getUrlEncoded();
    
    /**
     * Resets the URL - removes all query parameters
     * 
     * @return URL without any query parameters
     */
    public UrlInterface reset();
    
    /**
     * Adds a query parameter to the URL
     * 
     * @param key Name of the parameter. For instance "page" or "t[]"
     * @param value Value of the parameter. For instance "10" or "border:width=50,height=50"
     * @return URL with the query added
     */
    public UrlInterface addQueryParam(String key, String value);
    
    /**
     * Returns the URL with query parameters added
     * 
     * @return Full URL with query parameters, as a String
     */
    public String toString();
    
}