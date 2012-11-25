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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test the query class
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class QueryTest {

    private Query query;

    @Before
    public void setUp() {
        query = new Query();
    }

    @After
    public void tearDown() {
        query = null;
    }

    /**
     * The default page value must be 1
     */
    @Test
    public void testSetsADefaultPageValueOf1() {
        assertEquals(1, query.page());
    }

    /**
     * The default limit must be 20
     */
    @Test
    public void testSetsADefaultLimitValueOf20() {
        assertEquals(20, query.limit());
    }

    /**
     * By default we should not return meta data
     */
    @Test
    public void testSetsADefaultReturnmetadataValueOfFalse() {
        assertFalse(query.returnMetadata());
    }

    /**
     * By default, no meta data query should be set
     */
    @Test
    public void testSetsAnEmptyDefaultMetadataQuery() {
        assertNull(query.metadataQuery());
    }

    /**
     * By default, no "from" date should be set
     */
    @Test
    public void testSetsADefaultFromValueOfNull() {
        assertNull(query.from());
    }

    /**
     * By default, no "to" date should be set
     */
    @Test
    public void testSetsADefaultToValueOfNull() {
        assertNull(query.to());
    }

    /**
     * The query instance must be able to set and get the page value
     */
    @Test
    public void testCanSetAndGetThePageValue() {
        assertEquals(query, query.page(2));
        assertEquals(2, query.page());
    }

    /**
     * The query instance must be able to set and get the limit value
     */
    @Test
    public void testCanSetAndGetTheLimitValue() {
        assertEquals(query, query.limit(30));
        assertEquals(30, query.limit());
    }

    /**
     * The query instance must be able to set and get whether to return meta data
     */
    @Test
    public void testCanSetAndGetTheReturnmetadataValue() {
        assertEquals(query, query.returnMetadata(true));
        assertTrue(query.returnMetadata());
    }

    /**
     * The query instance must be able to set and get a meta data query
     *
     * @throws JSONException
     */
    @Test
    public void testCanSetAndGetAMetadataQuery() throws JSONException {
        JSONObject value = new JSONObject().put("category", "some category");
        assertEquals(query, query.metadataQuery(value));
        assertEquals(value, query.metadataQuery());
    }

    /**
     * The query instance must be able to set and get the "from" date
     */
    @Test
    public void testCanSetAndGetTheFromValue() {
        Date value = new Date();
        assertEquals(query, query.from(value));
        assertEquals(value, query.from());
    }

    /**
     * The query instance must be able to set and get the "to" date
     */
    @Test
    public void testCanSetAndGetTheToValue() {
        Date value = new Date();
        assertEquals(query, query.to(value));
        assertEquals(value, query.to());
    }

}