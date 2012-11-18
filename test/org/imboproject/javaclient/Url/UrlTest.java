/**
 * ImboClient-java
 *
 * Copyright (c) 2012-, Espen Hovlandsdal <espen@hovlandsdal.com>
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

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertSame;

import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

/**
 * Unit test for the abstract URL class methods 
 * 
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class UrlTest {

	@Rule
	public JUnitRuleMockery context = new JUnitRuleMockery();
	
	private Url url;
	private String baseUrl = "http://imbo";
	private String publicKey = "public";
	private String privateKey = "private";
	
	@Before
	public void setUp() {
		url = new UrlImplementation(this.baseUrl, this.publicKey, this.privateKey);
	}

	@After
	public void tearDown() {
		url = null;
	}

	/**
	 * The URL instance must return the complete url when used in a string context
	 */
	@Test
	public void testReturnsTheCompleteUrlWhenUsedInAStringContext() {
		String url = "" + this.url;
		assertEquals(this.url.toString(), url);
		assertThat(url, startsWith(this.baseUrl + "/resource"));
	}
	
	/**
	 * The URL instance must create an access token if one does not exist when first accessed
	 */
	@Test
	public void testCanAutomaticallyCreateAnAccessTokenInstanceIfOneIsNotSet() {
		assertThat(url.getAccessToken(), instanceOf(AccessTokenInterface.class));
	}
	
	/**
	 * The URL instance must be able to set and get an access token instance
	 */
	@Test
	public void testCanSetAndGetAnAccessTokenInstance() {
		final AccessTokenInterface accessToken = context.mock(AccessTokenInterface.class);
		
		assertSame(url, url.setAccessToken(accessToken));
		assertSame(accessToken, url.getAccessToken());
	}
	
	/**
	 * The URL instance must be able to add different query parameters that is added in the query string
	 */
	@Test
	public void testCanAddQueryParametersThatIsAddedInTheQueryString() {
		assertSame(url, url.addQueryParam("key", "value"));
		assertThat(url.getUrl(), startsWith(this.baseUrl + "/resource?key=value&accessToken="));
		assertThat(url.getUrlEncoded(), startsWith(this.baseUrl + "/resource?key=value&amp;accessToken="));
	}
	
	/**
	 * The URL instance must be able to reset the added query parameters
	 */
	@Test
	public void testAddMultipleQueryParamsAndReset() {
		assertSame(url, url.addQueryParam("t[]", "border"));
        assertSame(url, url.addQueryParam("query", "{\"foo\":\"bar\"}"));

        assertThat(
    		url.getUrl(),
    		startsWith("http://imbo/resource?t[]=border&query=%7B%22foo%22%3A%22bar%22%7D&accessToken=")
        );

        assertThat(
            url.getUrlEncoded(),
        	startsWith("http://imbo/resource?t%5B%5D=border&amp;query=%7B%22foo%22%3A%22bar%22%7D&amp;accessToken=")
        );

        assertSame(url, url.reset());
        
        assertThat(
    		url.getUrlEncoded(),
    		startsWith("http://imbo/resource?accessToken=")
		);
	}
	
	/**
	 * The URL instance does not append an access token if the public or private key is missing
	 */
	@Test
	public void testDoesNotAppendAccessTokenIfPublicOrPrivateKeyAreMissing() {
		UrlImplementation url = new UrlImplementation(baseUrl, null, null);
		assertEquals("http://imbo/resource", url.getUrl());
	}
	
	private class UrlImplementation extends Url {
		public UrlImplementation(String baseUrl, String publicKey, String privateKey) {
			super(baseUrl, publicKey, privateKey);
		}

		/**
		 * {@inheritDoc}
		 */
		protected String getResourceUrl() {
			return this.baseUrl + "/resource";
		}
	}
}
