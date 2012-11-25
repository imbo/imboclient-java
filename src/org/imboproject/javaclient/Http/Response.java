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

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Client response
 * 
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class Response implements ResponseInterface {

	/**
	 * Headers for the response
	 */
	private HashMap<String, String> headers;
	
	/**
	 * Body of the response
	 */
	private String body = "";
	
	/**
	 * Status code of the response
	 */
	private int statusCode;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public HashMap<String, String> getHeaders() {
		return headers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseInterface setHeaders(HashMap<String, String> headers) {
		this.headers = headers;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBody() {
		return this.body;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseInterface setBody(String body) {
		this.body = body;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getStatusCode() {
		return this.statusCode;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ResponseInterface setStatusCode(int code) {
		this.statusCode = code;
		return this;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getImboErrorCode() {
		if (body == null) {
			return 0;
		}
		
		try {
			JSONObject body = new JSONObject(this.body);
			if (!body.has("error")) {
				return 0;
			}
			
			JSONObject error = body.getJSONObject("error");
			if (!error.has("imboErrorCode")) {
				return 0;
			}
			
			return error.optInt("imboErrorCode", 0);
		} catch (JSONException e) {
			return 0;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSuccess() {
		int code = getStatusCode();
		
		return (code < 300 && code >= 200);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isError() {
		return getStatusCode() >= 400;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getImageIdentifier() {
		if (headers == null) {
			return null;
		}
		
		return headers.get("x-imbo-imageidentifier");
	}
	
	/**
	 * Returns the response as a string (only the response body)
	 * 
	 * @return The response body
	 */
	public String toString() {
		return body;
	}
	
	/**
	 * Returns the response body as a JSON object
	 * 
	 * @return Response body as a JSON object
	 */
	public JSONObject asJsonObject() {
		JSONObject body;
		try {
			body = new JSONObject(this.body);
		} catch (Exception e) {
			return null;
		}
		
		return body;
	}

}