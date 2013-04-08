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
package org.imboproject.javaclient;

import java.io.IOException;

import org.imboproject.javaclient.Http.ResponseInterface;

/**
 * Server exception
 * 
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class ServerException extends IOException {
	
	/**
	 * Serial UID
	 */
	private static final long serialVersionUID = 3243355260045940012L;
	
	/**
	 * Holds the response instance
	 */
	private ResponseInterface response;
	
	/**
	 * Holds the error code for this exception
	 */
	private int errorCode = 0;
	
	/**
	 * Exception constructor
	 * 
	 * @param errorMessage
	 */
	public ServerException(String errorMessage, int errorCode) {
		super(errorMessage);
		
		this.errorCode = errorCode;
	}

	/**
	 * Set the response instance
	 * 
	 * @param response Response object containing info about the server response
	 * @return This exception instance
	 */
	public ServerException setResponse(ResponseInterface response) {
		this.response = response;
		
		return this;
	}
	
	/**
	 * Get the response instance
	 * 
	 * @return ResponseInterface
	 */
	public ResponseInterface getResponse() {
		return this.response;
	}
	
	/**
	 * Get the error code for this exception
	 * 
	 * @return Error code
	 */
	public int getErrorCode() {
		return this.errorCode;
		
	}
	
}