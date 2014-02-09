/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package io.imbo.client.Http;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import io.imbo.client.Http.ImboResponse;

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

	private ImboResponse response;
	private int responseCode;
	private boolean expectSuccess;
	
	public ResponseTest(int code, boolean expected) {
		responseCode = code;
		expectSuccess = expected;
	}
	
	@Before
	public void setUp() {
		this.response = new ImboResponse();
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
	 * The response class must be able to set and get the content-type
	 */
	@Test
	public void testCanSetAndGetAContentType() {
		String contentType = "application/json";
		
		assertSame(response, response.setContentType(contentType));
		assertEquals(contentType, response.getContentType());
	}
	
	/**
	 * The response class must be able to set and get the content length
	 */
	@Test
	public void testCanSetAndGetContentLength() {
		int length = 1337;
		
		assertSame(response, response.setContentLength(length));
		assertEquals(length, response.getContentLength());
	}
	
	/**
	 * The response class must be able to set and get a raw body
	 */
	@Test
	public void testCanSetAndGetRawBody() {
		byte[] body = new byte[] { 1, 2, 3, 4, 5};
		
		assertSame(response, response.setRawBody(body));
		assertEquals(body, response.getRawBody());
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
    		new BasicHeader("X-Imbo-ImageIdentifier", imageIdentifier)
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
    	
    	response.setBody(null);
    	assertEquals(0, response.getImboErrorCode());
    	
    	response.setBody("foobar");
    	assertEquals(0, response.getImboErrorCode());
    	
    	response.setBody("{}");
    	assertEquals(0, response.getImboErrorCode());
    	
    	response.setBody("{\"error\":{\"code\":400}}");
    	assertEquals(0, response.getImboErrorCode());
    	
    	response.setBody("{\"error\":{\"imboErrorCode\":400}}");
    	assertEquals(400, response.getImboErrorCode());
    	
    	response.setBody("{\"error\":{\"imboErrorCode\":\"400\"}}");
    	assertEquals(400, response.getImboErrorCode());
    }
    
    /**
     * The response must return a reasonably verbose error description
     */
    @Test
    public void testCanReturnInformativeErrorDescription() {
    	assertEquals("Empty body", response.getImboErrorDescription());
    	
    	response.setBody(null);
    	assertEquals("Empty body", response.getImboErrorDescription());
    	
    	response.setBody("foobar");
    	assertEquals("A JSONObject text must begin with '{' at character 1", response.getImboErrorDescription());
    	
    	response.setBody("{}");
    	assertEquals("Error not specified", response.getImboErrorDescription());
    	
    	response.setBody("{\"error\":{\"code\":400}}");
    	assertEquals("Error message not specified", response.getImboErrorDescription());
    	
    	response.setBody("{\"error\":{\"message\":\"Zie error\"}}");
    	assertEquals("Zie error", response.getImboErrorDescription());
    }
    
    
}