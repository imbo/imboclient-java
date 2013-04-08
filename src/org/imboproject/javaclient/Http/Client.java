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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.LinkedList;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.imboproject.javaclient.ServerException;

/**
 * Imbo HTTP client
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class Client implements ClientInterface {

	/**
	 * Web client to use for requests
	 */
	private DefaultHttpClient webClient;

	/**
	 * HTTP parameters to use for requests
	 */
	private HttpParams httpParams;

	/**
	 * HTTP request headers
	 */
	private LinkedList<Header> requestHeaders;

	/**
	 * Response handler for the web client
	 */
	private ResponseHandler<Response> defaultHandler = new ResponseHandler<Response>() {
	    public Response handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
	        Response imboResponse = new Response();

	    	HttpEntity entity = response.getEntity();
	        Header contentType = entity.getContentType();

	        imboResponse.setStatusCode(response.getStatusLine().getStatusCode());
	        imboResponse.setContentType(contentType.getValue());
	        imboResponse.setContentLength(entity.getContentLength());
	        imboResponse.setHeaders(response.getAllHeaders());

	        if (contentType.getValue().startsWith("image/")) {
	        	imboResponse.setRawBody(EntityUtils.toByteArray(entity));
	        } else {
	        	imboResponse.setBody(EntityUtils.toString(entity));
	        }

	        return imboResponse;
	    }
	};

	/**
	 * Constructs the HTTP client
	 */
	public Client() {
		webClient = new DefaultHttpClient();

		httpParams = new BasicHttpParams();
		HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		HttpProtocolParams.setContentCharset(httpParams, "UTF_8");
		HttpProtocolParams.setUseExpectContinue(httpParams, false);
		HttpConnectionParams.setConnectionTimeout(httpParams, 20000);
		HttpConnectionParams.setSoTimeout(httpParams, 20000);

		webClient.setParams(httpParams);
	}

	/**
	 * Returns a set of HTTP parameters
	 *
	 * @return HTTP parameters
	 */
	public HttpParams getHttpParams() {
		return httpParams;
	}

	/**
	 * Set HTTP parameters
	 *
	 * @param params HTTP parameters to use for requests
	 * @return HTTP client instance
	 */
	public ClientInterface setHttpParams(HttpParams params) {
		webClient.setParams(params);

		return this;
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
    	
    	return request(post);
    }

    /**
     * {@inheritDoc}
     */
    public ResponseInterface get(URI url) throws IOException {
        return request(new HttpGet(url));
    }

    /**
     * {@inheritDoc}
     */
    public Response head(URI url) throws IOException {
    	return request(new HttpHead(url));
    }

    /**
     * {@inheritDoc}
     */
    public Response delete(URI url) throws IOException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Response put(URI url, String data) throws IOException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Response put(URI url, InputStream input) throws IOException {

    	return null;
    }

    /**
     * {@inheritDoc}
     */
    public Response put(URI url, File file) throws IOException {
        return put(url, new FileInputStream(file));
    }

    /**
     * {@inheritDoc}
     */
    public Client setRequestHeaders(LinkedList<Header> headers) {
        requestHeaders = headers;

    	return this;
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
    	Response response = webClient.execute(request, defaultHandler);

    	// Check for errors and throw exception if encountering any
    	if (response.isError()) {
    		ServerException exception = new ServerException(
				response.getImboErrorDescription(),
				response.getImboErrorCode()
			);
    		exception.setResponse(response);

    		throw exception;
    	}

    	// Return response
    	return response;
    }

}
