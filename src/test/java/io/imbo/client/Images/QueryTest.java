/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package io.imbo.client.Images;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import io.imbo.client.Images.Query;

import java.util.Date;
import java.util.HashMap;

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
    
    /**
     * The query instance must be able to convert set values to
     * a hashmap when all values have been specified
     * 
     * @throws JSONException 
     */
    @Test
    public void testCanConvertToHashMapWithValues() throws JSONException {
        Date from = new Date();
    	query.from(from);
    	
    	query.limit(5);
    	
    	JSONObject metadataQuery = new JSONObject();
    	metadataQuery.put("foo", "bar");
    	query.metadataQuery(metadataQuery);
    	
    	query.page(3);
    	query.returnMetadata(true);
    	
    	Date to = new Date();
        query.to(to);
        
        HashMap<String, String> map = query.toHashMap();
        assertEquals(Long.toString(from.getTime()), map.get("from"));
        assertEquals("5", map.get("limit"));
        assertEquals("{\"foo\":\"bar\"}", map.get("query"));
        assertEquals("3", map.get("page"));
        assertEquals("1", map.get("metadata"));
        assertEquals(Long.toString(to.getTime()), map.get("to"));
    }
    
    /**
     * The query instance must be able to return a blank hashmap
     * if no values have been set
     * 
     * @throws JSONException 
     */
    @Test
    public void testCanConvertToHashMapWithNoValues() throws JSONException {
        query.limit(0);
        query.page(0);
    	
    	HashMap<String, String> map = query.toHashMap();
        assertTrue(map.isEmpty());
    }

}