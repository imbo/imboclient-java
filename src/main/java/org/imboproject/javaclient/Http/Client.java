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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.imboproject.javaclient.ServerException;
import org.imboproject.javaclient.Url.UrlInterface;

/**
 * Imbo HTTP client
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class Client implements ClientInterface {

	/**
	 * Web client to use for requests
	 */
	private HttpClient webClient;

	/**
	 * HTTP parameters to use for requests
	 */
	private HttpParams httpParams;

	/**
	 * HTTP request headers
	 */
	private Header[] requestHeaders = new Header[] {};

	/**
	 * Response handler for the web client
	 */
	private ResponseHandler<Response> defaultHandler = new ResponseHandler<Response>() {
	    public Response handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
	        Response imboResponse = new Response();

	    	HttpEntity entity    = response.getEntity();
	        Header contentType   = response.getFirstHeader("Content-Type");
	        Header contentLength = response.getFirstHeader("Content-Length");
	        
	        imboResponse.setStatusCode(response.getStatusLine().getStatusCode());
	        imboResponse.setHeaders(response.getAllHeaders());
	        
	        if (contentType != null) {
	        	imboResponse.setContentType(contentType.getValue().split(";")[0].trim());
	        }
	        
	        if (contentLength != null) {
	        	imboResponse.setContentLength(Long.parseLong(contentLength.getValue()));
	        }

	        if (contentType.getValue().startsWith("image/")) {
	        	imboResponse.setRawBody(EntityUtils.toByteArray(entity));
	        } else if (entity != null) {
	        	imboResponse.setBody(EntityUtils.toString(entity));
	        }

	        return imboResponse;
	    }
	};
	
	/**
     * {@inheritDoc}
     */
	public Response post(UrlInterface url, String data) throws IOException {
		return post(url, data, null);
	}
	
	/**
     * {@inheritDoc}
     */
	public Response post(UrlInterface url, String data, Header[] headers) throws IOException {
		return post(url.toUri(), data, headers);
	}
	
	/**
     * {@inheritDoc}
     */
    public Response post(URI url, String data) throws IOException {
    	return post(url, data, null);
    }

    /**
     * {@inheritDoc}
     */
    public Response post(URI url, String data, Header[] headers) throws IOException {
    	HttpPost post = new HttpPost(url);
    	post.setEntity(new StringEntity(data));
    	
    	if (headers != null) {
    		post.setHeaders(headers);
    	}
    	
    	return this.request(post);
    }

    /**
     * {@inheritDoc}
     */
    public Response get(URI url) throws IOException {
        return this.request(new HttpGet(url));
    }
    
    /**
     * {@inheritDoc}
     */
	public Response get(UrlInterface url) throws IOException {
		return this.get(url.toUri());
	}

    /**
     * {@inheritDoc}
     */
    public Response head(URI url) throws IOException {
    	return this.request(new HttpHead(url));
    }
    
    /**
     * {@inheritDoc}
     */
    public Response head(UrlInterface url) throws IOException {
    	return this.head(url.toUri());
    }

    /**
     * {@inheritDoc}
     */
    public Response delete(URI url) throws IOException {
        return this.request(new HttpDelete(url));
    }
    
    /**
     * {@inheritDoc}
     */
    public Response delete(UrlInterface url) throws IOException {
        return this.delete(url.toUri());
    }

    /**
     * {@inheritDoc}
     */
    public Response put(URI url, String data) throws IOException {
        return this.put(url, data, null);
    }
    
    /**
     * {@inheritDoc}
     */
	public Response put(URI url, String data, Header[] headers) throws IOException {
		HttpPut put = new HttpPut(url);
		put.setEntity(new StringEntity(data));
    	
    	if (headers != null) {
    		put.setHeaders(headers);
    	}
    	
    	return this.request(put);
	}

    /**
     * {@inheritDoc}
     */
    public Response put(URI url, InputStream input) throws IOException {
    	return this.put(url, input, null);
    }
    
    /**
     * {@inheritDoc}
     */
    public Response put(URI url, InputStream input, Header[] headers) throws IOException {
    	byte[] data = readInputStream(input);
    	
    	HttpPut put = new HttpPut(url);
		put.setEntity(new ByteArrayEntity(data));
    	
    	if (headers != null) {
    		put.setHeaders(headers);
    	}
    	
    	return this.request(put);
    }

    /**
     * {@inheritDoc}
     */
    public Response put(URI url, File file) throws IOException {
        return this.put(url, file, null);
    }
    
    /**
     * {@inheritDoc}
     */
    public Response put(URI url, File file, Header[] headers) throws IOException {
        return this.put(url, new FileInputStream(file));
    }
    
    /**
     * {@inheritDoc}
     */
    public Response put(UrlInterface url, String data) throws IOException {
        return this.put(url.toUri(), data, null);
    }
    
    /**
     * {@inheritDoc}
     */
	public Response put(UrlInterface url, String data, Header[] headers) throws IOException {
		return this.put(url.toUri(), data, headers);
	}

    /**
     * {@inheritDoc}
     */
    public Response put(UrlInterface url, InputStream input) throws IOException {
    	return this.put(url.toUri(), input, null);
    }
    
    /**
     * {@inheritDoc}
     */
    public Response put(UrlInterface url, InputStream input, Header[] headers) throws IOException {
    	return this.put(url.toUri(), input, headers);
    }

    /**
     * {@inheritDoc}
     */
    public Response put(UrlInterface url, File file) throws IOException {
        return this.put(url, file, null);
    }
    
    /**
     * {@inheritDoc}
     */
    public Response put(UrlInterface url, File file, Header[] headers) throws IOException {
        return this.put(url.toUri(), file, headers);
    }

    /**
     * {@inheritDoc}
     */
    public Client setRequestHeaders(Header[] headers) {
        requestHeaders = headers;

    	return this;
    }
    
    /**
     * {@inheritDoc}
     */
    public Header[] getRequestHeaders() {
        return this.requestHeaders;
    }
    
	/**
	 * Returns a set of HTTP parameters
	 *
	 * @return HTTP parameters
	 */
	public HttpClient getHttpClient() {
		if (webClient == null) {
			this.setHttpClient(getDefaultHttpClient());
		}
		
		return webClient;
	}

	/**
	 * Set HTTP parameters
	 *
	 * @param params HTTP parameters to use for requests
	 * @return HTTP client instance
	 */
	public ClientInterface setHttpClient(HttpClient httpClient) {
		webClient = httpClient;

		return this;
	}
	
	/**
	 * Get the default response handler
	 * 
	 * @return Default response handler
	 */
	public ResponseHandler<Response> getResponseHandler() {
		return defaultHandler;
	}

    /**
     * Perform a request of the given HTTP method against the given URL
     *
     * @param request Request to perform
     * @return HTTP response
     */
    protected Response request(HttpRequestBase request) throws IOException {
		// Add request headers to outgoing request
    	for (Header header : requestHeaders) {
    		request.addHeader(header);
    	}

    	// Perform request using default handler
    	Response response = getHttpClient().execute(request, defaultHandler);

    	// Check for errors and throw exception if encountering any
    	if (response.isError()) {
    		ServerException exception = new ServerException(
				response.getImboErrorDescription(),
				response.getStatusCode()
			);
    		exception.setResponse(response);

    		throw exception;
    	}

    	// Return response
    	return response;
    }
    
    /**
     * Get a default HTTP client
     * 
     * @return Default HTTP params with some basic options set
     */
    protected HttpClient getDefaultHttpClient() {
    	httpParams = new BasicHttpParams();
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, "UTF_8");
		HttpProtocolParams.setUseExpectContinue(httpParams, false);
		HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
		HttpConnectionParams.setSoTimeout(httpParams, 20000);
		
		DefaultHttpClient client = new DefaultHttpClient();
		client.setParams(httpParams);
		
		return client;
    }
    
    /**
     * Read input stream into a byte array
     * 
     * @param input Input stream to read from
     * @return Byte array with the contents of the input stream
     * @throws IOException
     */
    protected byte[] readInputStream(InputStream input) throws IOException {
    	ByteArrayOutputStream buffer = new ByteArrayOutputStream();

    	int read;
    	byte[] data = new byte[16384];

    	while ((read = input.read(data, 0, data.length)) != -1) {
    		buffer.write(data, 0, read);
    	}

    	return buffer.toByteArray();
    }

}