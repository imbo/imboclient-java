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
package org.imboproject.javaclient.Images;

import java.util.Date;

import org.json.JSONObject;

/**
 * Images query interface
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public interface QueryInterface {

    /**
     * Get the page property
     *
     * @return Page number
     */
    public int page();

    /**
     * Set the page property
     *
     * @param pageNum Page number
     * @return Returns this query instance
     */
    public QueryInterface page(int pageNum);

    /**
     * Get the limit property
     *
     * @return Max number of items
     */
    public int limit();

    /**
     * Set the limit property
     *
     * @param limit Max number of items
     * @return Returns this query instance
     */
    public QueryInterface limit(int limit);

    /**
     * Get whether to return the meta data for each image along with the base image data
     *
     * @return Whether to return meta data or not
     */
    public boolean returnMetadata();

    /**
     * Set whether to return the meta data for each image along with the base image data
     *
     * @param returnMetadata True if meta data is wanted, false otherwise
     * @return Returns this query instance
     */
    public QueryInterface returnMetadata(boolean returnMetadata);

    /**
     * Get the meta data query sent along with this request
     *
     * @return The meta data query
     */
    public JSONObject metadataQuery();

    /**
     * Set the meta data query sent along with this request
     *
     * @param metadataQuery The meta data query as a JSON object
     * @return Returns this query instance
     */
    public QueryInterface metadataQuery(JSONObject metadataQuery);

    /**
     * Gets the lower date limit to fetch from
     *
     * @return Date to fetch from
     */
    public Date from();

    /**
     * Sets the date to fetch from when querying
     *
     * @param from The lower date limit to fetch from
     * @return Returns this query instance
     */
    public QueryInterface from(Date from);

    /**
     * Gets the upper date limit to fetch from
     *
     * @return Date to fetch to
     */
    public Date to();

    /**
     * Sets the date to fetch to when querying
     *
     * @param to The upper date limit to fetch to
     * @return Returns this query instance
     */
    public QueryInterface to(Date to);

}