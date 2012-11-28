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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Client response test
 * 
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
@RunWith(Parameterized.class)
public class ResponseTest {

	private Response response;
	private int responseCode;
	private boolean expectSuccess;
	
	public ResponseTest(int code, boolean expected) {
		responseCode = code;
		expectSuccess = expected;
	}
	
	@Before
	public void setUp() {
		this.response = new Response();
	}

	@After
	public void tearDown() {
		this.response = null;
	}
	
	/**
	 * The response class must be able to set and get headers
	 */
	@Test
	public void testCanSetAndGetAHeaders() {
		Header[] testHeaders = {
			new BasicHeader("foo", "bar")
		};
		
		assertSame(response, response.setHeaders(testHeaders));
		assertEquals("bar", response.getHeaders().get("foo"));
	}
	
	/**
	 * The response class must be able to set and get the body
	 */
	@Test
	public void testCanSetAndGetABody() {
		String body = "Content";
		
		assertSame(response, response.setBody(body));
		assertEquals(body, response.getBody());
	}
	
	/**
	 * The response class must be able to set and get a status code
	 */
	@Test
	public void testCanSetAndGetAStatusCode() {
		int code = 404;
		
		assertSame(response, response.setStatusCode(code));
		assertEquals(code, response.getStatusCode());
	}
	
	@Parameterized.Parameters
    public static List<Object[]> getResponseCodeSuccessData() {
        return Arrays.asList(new Object[][] {
        		{ 200, true },
        		{ 300, false },
        		{ 400, false },
        		{ 500, false },
        		{ 201, true }
        });
    }
	
    /**
     * The response must tell whether the response is a "success" based on an HTTP status code
     */
    @Test
	public void testCanTellWhetherOrNotTheResponseIsASuccessBasedOnAnHttpStatusCode() {
		response.setStatusCode(responseCode);
		assertEquals(expectSuccess, response.isSuccess());
	}

    /**
     * The response must tell whether the response is an "error" based on an HTTP status code
     */
    @Test
    public void testCanTellWhetherOrNotTheResponseIsAnErrorBasedOnAnHttpStatusCode() {
    	boolean expected = responseCode == 300 ? false : !expectSuccess;
    	response.setStatusCode(responseCode);
    	assertEquals(expected, response.isError());
    }
    
    /**
     * The response must return the body in a string context
     */
    @Test
    public void testReturnsTheBodyWhenUsedInAStringContext() {
    	assertEquals("", response.toString());
    	String body = "Body content";
    	response.setBody(body);
    	assertEquals(body, response.toString());
    }
    
    /**
     * The response must be able to return the image identifier if it exists in the headers
     */
    @Test
    public void testCanFetchAnImageIdentifierIfItExistsAsAHeader() {
    	String imageIdentifier = "57cc615a80f6c623a138846cf7509028";
    	
    	assertNull(response.getImageIdentifier());
    	
    	Header[] headers = {
    		new BasicHeader("x-imbo-imageidentifier", imageIdentifier)
    	};
		
		response.setHeaders(headers);
		
		assertEquals(imageIdentifier, response.getImageIdentifier());
    }
    
    /**
     * Can return the JSON encoded body as a JSON object
     */
    @Test
    public void testCanReturnAJsonEncodedBodyAsAJsonObject() {
    	assertNull(response.asJsonObject());
    	
    	response.setBody("{\"foo\":\"bar\"}");
    	JSONObject body = response.asJsonObject();
    	assertEquals("bar", body.optString("foo", null));
    }
    
    /**
     * The response must return the imbo error code only if the response body
     * has a correct error element
     */
    @Test
    public void testCanReturnAnImboErrorCodeWhenTheBodyHasAnErrorElement() {
    	assertEquals(0, response.getImboErrorCode());
    	
    	response.setBody("foobar");
    	assertEquals(0, response.getImboErrorCode());
    	
    	response.setBody("{\"error\":{\"code\":400}}");
    	assertEquals(0, response.getImboErrorCode());
    	
    	response.setBody("{\"error\":{\"imboErrorCode\":400}}");
    	assertEquals(400, response.getImboErrorCode());
    	
    	response.setBody("{\"error\":{\"imboErrorCode\":\"400\"}}");
    	assertEquals(400, response.getImboErrorCode());
    }
}