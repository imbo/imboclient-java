/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package io.imbo.client.Url;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import io.imbo.client.Url.AccessToken;
import io.imbo.client.Url.ImboUrl;

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
public class ImboUrlTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();
    
    private ImboUrl url;
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
        assertThat(url.getAccessToken(), instanceOf(AccessToken.class));
    }
    
    /**
     * The URL instance must be able to set and get an access token instance
     */
    @Test
    public void testCanSetAndGetAnAccessTokenInstance() {
        final AccessToken accessToken = context.mock(AccessToken.class);
        
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
    
    private class UrlImplementation extends ImboUrl {
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
