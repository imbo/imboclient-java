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
 * Query class for the images resource
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class Query implements QueryInterface {

    /**
     * The page to get
     */
    private int page = 1;

    /**
     * The maximum number of images to get
     */
    private int limit = 20;

    /**
     * Whether to return meta data for the images or not
     */
    private boolean returnMetadata = false;

    /**
     * Meta data query
     */
    private JSONObject metadataQuery = null;

    /**
     * Date to start fetching from
     */
    private Date from;

    /**
     * Date to stop fetching at
     */
    private Date to;

    /**
     * {@inheritDoc}
     */
    public int page() {
        return page;
    }

    /**
     * {@inheritDoc}
     */
    public QueryInterface page(int pageNum) {
        this.page = pageNum;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public int limit() {
        return limit;
    }

    /**
     * {@inheritDoc}
     */
    public QueryInterface limit(int limit) {
        this.limit = limit;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public boolean returnMetadata() {
        return returnMetadata;
    }

    /**
     * {@inheritDoc}
     */
    public QueryInterface returnMetadata(boolean returnMetadata) {
        this.returnMetadata = returnMetadata;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public JSONObject metadataQuery() {
        return metadataQuery;
    }

    /**
     * {@inheritDoc}
     */
    public QueryInterface metadataQuery(JSONObject metadataQuery) {
        this.metadataQuery = metadataQuery;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Date from() {
        return from;
    }

    /**
     * {@inheritDoc}
     */
    public QueryInterface from(Date from) {
        this.from = from;
        return this;
    }

    /**
     * {@inheritDoc}
     */
    public Date to() {
        return to;
    }

    /**
     * {@inheritDoc}
     */
    public QueryInterface to(Date to) {
        this.to = to;
        return this;
    }

}