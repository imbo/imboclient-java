/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package org.imboproject.javaclient.Http;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.imboproject.javaclient.util.PostBodyMatches.PostBodyMatches;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.imboproject.javaclient.ServerException;
import org.imboproject.javaclient.Url.StatusUrl;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Client response test
 * 
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class ClientTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    public final HttpClient webClient = context.mock(HttpClient.class); 
    
    private Client client;
    
    @Before
    public void setUp() {
        this.client = new Client();
    }

    @After
    public void tearDown() {
        this.client = null;
    }
    
    /**
     * The response class must be able to set and get http client
     */
    @Test
    public void testCanSetAndGetHttpClient() {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        
        assertEquals(client, client.setHttpClient(httpClient));
        assertEquals(httpClient, client.getHttpClient());
    }
    
    /**
     * The response class must be able to set and get request headers
     */
    @Test
    public void testCanSetAndGetRequestHeaders() {
        Header foo = new BasicHeader("foo", "bar");
        Header bar = new BasicHeader("bar", "foo");
        Header[] headers = new Header[] {
            foo,
            bar
        };
        
        assertEquals(client, client.setRequestHeaders(headers));
        
        Header[] reqHeaders = client.getRequestHeaders();
        assertEquals(headers.length, reqHeaders.length);
        
        for (int i = 0; i < headers.length; i++) {
            assertEquals(headers[i], reqHeaders[i]);
        }
    }
    
    /**
     * The response class must be able to get an HTTP resource
     */
    @Test
    public void testCanGetHttpResource() throws IOException, URISyntaxException {
        useMockWebClient();
        final Response response = new Response();
        
        context.checking(new Expectations() {{
            oneOf(webClient).execute(with(any(HttpGet.class)), with(any(ResponseHandler.class)));
            will(returnValue(response));
        }});
        
        this.client.get(new URI("http://imbo-project.org/"));
    }
    
    @Test
    public void testCanGetHttpResourceWithAnUrlClass() throws IOException, URISyntaxException {
        useMockWebClient();
        final Response response = new Response();
        
        context.checking(new Expectations() {{
            oneOf(webClient).execute(with(any(HttpGet.class)), with(any(ResponseHandler.class)));
            will(returnValue(response));
        }});
        
        this.client.get(new StatusUrl("http://imbo/"));
    }
    
    @Test
    public void testCanHeadHttpResource() throws IOException, URISyntaxException {
        useMockWebClient();
        final Response response = new Response();
        
        context.checking(new Expectations() {{
            oneOf(webClient).execute(with(any(HttpHead.class)), with(any(ResponseHandler.class)));
            will(returnValue(response));
        }});
        
        this.client.head(new URI("http://imbo-project.org/"));
    }
    
    @Test
    public void testCanHeadHttpResourceWithAnUrlClass() throws IOException, URISyntaxException {
        useMockWebClient();
        final Response response = new Response();
        
        context.checking(new Expectations() {{
            oneOf(webClient).execute(with(any(HttpHead.class)), with(any(ResponseHandler.class)));
            will(returnValue(response));
        }});
        
        this.client.head(new StatusUrl("http://imbo"));
    }
    
    @Test
    public void testCanDeleteHttpResource() throws IOException, URISyntaxException {
        useMockWebClient();
        final Response response = new Response();
        
        context.checking(new Expectations() {{
            oneOf(webClient).execute(with(any(HttpDelete.class)), with(any(ResponseHandler.class)));
            will(returnValue(response));
        }});
        
        this.client.delete(new URI("http://imbo-project.org/"));
    }
    
    @Test
    public void testCanDeleteHttpResourceWithAnUrlClass() throws IOException, URISyntaxException {
        useMockWebClient();
        final Response response = new Response();
        
        context.checking(new Expectations() {{
            oneOf(webClient).execute(with(any(HttpDelete.class)), with(any(ResponseHandler.class)));
            will(returnValue(response));
        }});
        
        this.client.delete(new StatusUrl("http://imbo"));
    }
    
    @Test
    public void testCanPostHttpResource() throws IOException, URISyntaxException {
        useMockWebClient();
        final Response response = new Response();
        
        context.checking(new Expectations() {{
            oneOf(webClient).execute(
            	with(postBodyMatches("payload")),
            	with(any(ResponseHandler.class))
            );
            will(returnValue(response));
        }});
        
        this.client.post(new URI("http://imbo-project.org/"), "payload");
    }
    
    @Test
    public void testCanPostHttpResourceWithAnUrlClass() throws IOException, URISyntaxException {
        useMockWebClient();
        final Response response = new Response();
        final String body = "{\"foo\":\"bar\"}";
        final Header[] headers = new Header[] {
        	new BasicHeader("Content-Type", "application/json")	
        };
        
        context.checking(new Expectations() {{
        	exactly(2).of(webClient).execute(
            	with(postBodyMatches(body)),
            	with(any(ResponseHandler.class))
            );
            will(returnValue(response));
            
        }});
        
        this.client.post(new StatusUrl("http://imbo"), body);
        this.client.post(new StatusUrl("http://imbo"), body, headers);
    }
    
    @Test
    public void testCanPutHttpResource() throws IOException, URISyntaxException {
        useMockWebClient();
        final Response response = new Response();
        final File file = new File("misc/imbo-logo.png");
        final byte[] body = new byte[] { 1, 2, 3, 4, 5 };
        ByteArrayInputStream input = new ByteArrayInputStream(body);
        
        context.checking(new Expectations() {{
            exactly(3).of(webClient).execute(
            	with(any(HttpPut.class)),
            	with(any(ResponseHandler.class))
            );
            will(returnValue(response));
        }});
        
        this.client.put(new URI("http://imbo-project.org/"), "foo");
        this.client.put(new URI("http://imbo-project.org/"), input);
        this.client.put(new URI("http://imbo-project.org/"), file);
    }
    
    @Test
    public void testCanPutHttpResourceWithAnUrlClass() throws IOException, URISyntaxException {
        useMockWebClient();
        final Response response = new Response();
        final byte[] body = new byte[] { 1, 2, 3, 4, 5 };
        final File file = new File("misc/imbo-logo.png");
        final Header[] headers = new Header[] {
        	new BasicHeader("Content-Type", "image/jpg")
        };
        ByteArrayInputStream input = new ByteArrayInputStream(body);
        
        context.checking(new Expectations() {{
        	exactly(6).of(webClient).execute(
            	with(any(HttpPut.class)),
            	with(any(ResponseHandler.class))
            );
            will(returnValue(response));
            
        }});
        
        this.client.put(new StatusUrl("http://imbo"), input);
        this.client.put(new StatusUrl("http://imbo"), input, headers);
        this.client.put(new StatusUrl("http://imbo"), "foo");
        this.client.put(new StatusUrl("http://imbo"), "foo", headers);
        this.client.put(new StatusUrl("http://imbo"), file);
        this.client.put(new StatusUrl("http://imbo"), file, headers);
    }
    
    @Test
    public void testResponeHandlerHandlesImagesProperly() throws IOException, URISyntaxException {
    	final HttpResponse response = getResponseMock();
        final HttpEntity entity = getEntityMock();
        final String content = "foo";
        final BasicHeader contentType = new BasicHeader("Content-Type", "image/jpg");
        final BasicHeader contentLength = new BasicHeader("Content-Length", content.length() + "");
        final ByteArrayInputStream entityStream = new ByteArrayInputStream(content.getBytes());
        final StatusLine statusLine = new StatusLine() {
			public int getStatusCode() { return 200; }
			public String getReasonPhrase() { return null; }
			public ProtocolVersion getProtocolVersion() { return null; }
		};
		
        context.checking(new Expectations() {{
            atLeast(1).of(response).getFirstHeader("Content-Type");
            will(returnValue(contentType));
            
            atLeast(1).of(response).getLastHeader("Content-Length");
            will(returnValue(contentLength));
            
            atLeast(1).of(response).getEntity();
            will(returnValue(entity));
            
            atLeast(1).of(entity).getContentLength();
            will(returnValue((long) content.length()));
            
            atLeast(1).of(entity).getContent();
            will(returnValue(entityStream));
        	
            atLeast(1).of(response).getStatusLine();
            will(returnValue(statusLine));
            
            oneOf(response).getAllHeaders();
            will(returnValue(new Header[] { contentType }));
        }});
        
        ResponseHandler<Response> handler = this.client.getResponseHandler();
        Response parsed = handler.handleResponse(response);
        
        assertEquals("image/jpg", parsed.getContentType());
        assertEquals((long) 3, parsed.getContentLength());
        assertEquals(200, parsed.getStatusCode());
        assertArrayEquals(content.getBytes(), parsed.getRawBody());
        
        HashMap<String, String> headers = parsed.getHeaders();
        assertEquals("image/jpg", headers.get("Content-Type"));
    }
    
    @Test
    public void testResponseHandlerHandlesJsonRequests() throws IOException, URISyntaxException {
    	final HttpResponse response = getResponseMock();
        final HttpEntity entity = getEntityMock();
        final String content = "{\"foo\":\"bar\"}";
        final BasicHeader contentType = new BasicHeader("Content-Type", "application/json");
        final BasicHeader contentLength = new BasicHeader("Content-Length", content.length() + "");
        final ByteArrayInputStream entityStream = new ByteArrayInputStream(content.getBytes());
        final StatusLine statusLine = new StatusLine() {
			public int getStatusCode() { return 200; }
			public String getReasonPhrase() { return null; }
			public ProtocolVersion getProtocolVersion() { return null; }
		};
        
        context.checking(new Expectations() {{
        	atLeast(1).of(response).getFirstHeader("Content-Type");
            will(returnValue(contentType));
            
            atLeast(1).of(response).getLastHeader("Content-Length");
            will(returnValue(contentLength));
            
            atLeast(1).of(response).getEntity();
            will(returnValue(entity));
            
            atLeast(1).of(entity).getContentLength();
            will(returnValue((long) content.length()));
            
            atLeast(1).of(entity).getContent();
            will(returnValue(entityStream));
        	
            atLeast(1).of(response).getStatusLine();
            will(returnValue(statusLine));
            
            atLeast(1).of(entity).getContentType();
            will(returnValue(contentType));
            
            oneOf(response).getAllHeaders();
            will(returnValue(new Header[] { contentType }));
        }});
        
        ResponseHandler<Response> handler = this.client.getResponseHandler();
        Response parsed = handler.handleResponse(response);
        
        assertEquals("application/json", parsed.getContentType());
        assertEquals((long) 13, parsed.getContentLength());
        assertEquals(200, parsed.getStatusCode());
        assertEquals(content, parsed.getBody());
        
        HashMap<String, String> headers = parsed.getHeaders();
        assertEquals("application/json", headers.get("Content-Type"));
    }
    
    @Test
    public void testCanGetDefaultWebClient() {
    	assertThat(
    		this.client.getHttpClient(),
            instanceOf(HttpClient.class)
        );
    }
    
    @Test
    public void testClientThrowsServerExceptionOnError() throws IOException {
    	useMockWebClient();
    	
    	exception.expect(ServerException.class);
        exception.expectMessage("All out of beer");

        final Response response = new Response();
        response.setBody("{\"error\":{\"message\":\"All out of beer\"}}");
        response.setStatusCode(400);
        
        context.checking(new Expectations() {{
            oneOf(webClient).execute(with(any(HttpGet.class)), with(any(ResponseHandler.class)));
            will(returnValue(response));
        }});
        
        Header[] headers = new Header[] {
        	new BasicHeader("X-Imbo-Client", "imboclient-java")
        };
        
        this.client.setRequestHeaders(headers);
        this.client.get(new StatusUrl("http://imbo/"));
        
    }

    private int mockCount = 0;
    private HttpResponse getResponseMock() {
        return context.mock(HttpResponse.class, "response" + (++mockCount));
    }
    
    private HttpEntity getEntityMock() {
        return context.mock(HttpEntity.class, "entity" + (++mockCount));
    }
    
    private void useMockWebClient() {
        this.client.setHttpClient(webClient);
    }
    
    private static <T> org.hamcrest.Matcher<HttpPost> postBodyMatches(String postBody) {
        return PostBodyMatches(postBody);
    }

}