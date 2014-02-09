/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package io.imbo.client.Http;

import static io.imbo.client.util.PostBodyMatches.PostBodyMatches;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import io.imbo.client.ServerException;
import io.imbo.client.Url.StatusUrl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
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
public class HttpClientTest {

    @Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Rule
    public ExpectedException exception = ExpectedException.none();
    
    public final org.apache.http.client.HttpClient webClient = context.mock(org.apache.http.client.HttpClient.class); 
    
    private ImboHttpClient client;
    
    @Before
    public void setUp() {
        this.client = new ImboHttpClient();
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
        ArrayList<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("foo", "bar"));
        headers.add(new BasicHeader("bar", "foo"));
        
        assertEquals(client, client.setRequestHeaders(headers));
        
        List<Header> reqHeaders = client.getRequestHeaders();
        assertEquals(headers.size(), reqHeaders.size());
        
        for (int i = 0; i < headers.size(); i++) {
            assertEquals(headers.get(i), reqHeaders.get(i));
        }
    }
    
    /**
     * The response class must be able to get an HTTP resource
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testCanGetHttpResource() throws IOException, URISyntaxException {
        useMockWebClient();
        final ImboResponse response = new ImboResponse();
        
        context.checking(new Expectations() {{
            oneOf(webClient).execute(with(any(HttpGet.class)), with(any(ResponseHandler.class)));
            will(returnValue(response));
        }});
        
        this.client.get(new URI("http://imbo-project.org/"));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testCanGetHttpResourceWithAnUrlClass() throws IOException, URISyntaxException {
        useMockWebClient();
        final ImboResponse response = new ImboResponse();
        
        context.checking(new Expectations() {{
            oneOf(webClient).execute(with(any(HttpGet.class)), with(any(ResponseHandler.class)));
            will(returnValue(response));
        }});
        
        this.client.get(new StatusUrl("http://imbo/"));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testCanHeadHttpResource() throws IOException, URISyntaxException {
        useMockWebClient();
        final ImboResponse response = new ImboResponse();
        
        context.checking(new Expectations() {{
            oneOf(webClient).execute(with(any(HttpHead.class)), with(any(ResponseHandler.class)));
            will(returnValue(response));
        }});
        
        this.client.head(new URI("http://imbo-project.org/"));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testCanHeadHttpResourceWithAnUrlClass() throws IOException, URISyntaxException {
        useMockWebClient();
        final ImboResponse response = new ImboResponse();
        
        context.checking(new Expectations() {{
            oneOf(webClient).execute(with(any(HttpHead.class)), with(any(ResponseHandler.class)));
            will(returnValue(response));
        }});
        
        this.client.head(new StatusUrl("http://imbo"));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testCanDeleteHttpResource() throws IOException, URISyntaxException {
        useMockWebClient();
        final ImboResponse response = new ImboResponse();
        
        context.checking(new Expectations() {{
            oneOf(webClient).execute(with(any(HttpDelete.class)), with(any(ResponseHandler.class)));
            will(returnValue(response));
        }});
        
        this.client.delete(new URI("http://imbo-project.org/"));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testCanDeleteHttpResourceWithAnUrlClass() throws IOException, URISyntaxException {
        useMockWebClient();
        final ImboResponse response = new ImboResponse();
        
        context.checking(new Expectations() {{
            oneOf(webClient).execute(with(any(HttpDelete.class)), with(any(ResponseHandler.class)));
            will(returnValue(response));
        }});
        
        this.client.delete(new StatusUrl("http://imbo"));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testCanPostHttpResource() throws IOException, URISyntaxException {
        useMockWebClient();
        final ImboResponse response = new ImboResponse();
        
        context.checking(new Expectations() {{
            oneOf(webClient).execute(
            	with(postBodyMatches("payload")),
            	with(any(ResponseHandler.class))
            );
            will(returnValue(response));
        }});
        
        this.client.post(new URI("http://imbo-project.org/"), "payload");
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testCanPostHttpResourceWithAnUrlClass() throws IOException, URISyntaxException {
        useMockWebClient();
        final ImboResponse response = new ImboResponse();
        final String body = "{\"foo\":\"bar\"}";
        final ArrayList<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Content-Type", "application/json"));
        
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
    
    @SuppressWarnings("unchecked")
    @Test
    public void testCanPutHttpResource() throws IOException, URISyntaxException {
        useMockWebClient();
        final ImboResponse response = new ImboResponse();
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
    
    @SuppressWarnings("unchecked")
    @Test
    public void testCanPutHttpResourceWithAnUrlClass() throws IOException, URISyntaxException {
        useMockWebClient();
        final ImboResponse response = new ImboResponse();
        final byte[] body = new byte[] { 1, 2, 3, 4, 5 };
        final File file = new File("misc/imbo-logo.png");
        final ArrayList<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Content-Type", "image/png"));
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
        
        ResponseHandler<ImboResponse> handler = this.client.getResponseHandler();
        ImboResponse parsed = handler.handleResponse(response);
        
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
        
        ResponseHandler<ImboResponse> handler = this.client.getResponseHandler();
        ImboResponse parsed = handler.handleResponse(response);
        
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
            instanceOf(org.apache.http.client.HttpClient.class)
        );
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testClientThrowsServerExceptionOnError() throws IOException {
    	useMockWebClient();
    	
    	exception.expect(ServerException.class);
        exception.expectMessage("All out of beer");

        final ImboResponse response = new ImboResponse();
        response.setBody("{\"error\":{\"message\":\"All out of beer\"}}");
        response.setStatusCode(400);
        
        context.checking(new Expectations() {{
            oneOf(webClient).execute(with(any(HttpGet.class)), with(any(ResponseHandler.class)));
            will(returnValue(response));
        }});
        
        final ArrayList<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("X-Imbo-Client", "imboclient-java"));
        
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