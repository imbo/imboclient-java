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

/**
 * Client response interface 
 * 
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public interface ResponseInterface {

	/**
	 * Internal error codes sent from the Imbo server
	 */
	public static final int ERR_UNSPECIFIED = 0;

    // Authentication errors
    public static final int AUTH_UNKNOWN_PUBLIC_KEY    = 100;
    public static final int AUTH_MISSING_PARAM         = 101;
    public static final int AUTH_INVALID_TIMESTAMP     = 102;
    public static final int AUTH_SIGNATURE_MISMATCH    = 103;

    // Image resource errors
    public static final int IMAGE_ALREADY_EXISTS       = 200;
    public static final int IMAGE_NO_IMAGE_ATTACHED    = 201;
    public static final int IMAGE_HASH_MISMATCH        = 202;
    public static final int IMAGE_UNSUPPORTED_MIMETYPE = 203;
    public static final int IMAGE_BROKEN_IMAGE         = 204;
    
    /**
     * Get the headers for the HTTP response
     * 
     * @return Headers in key => value hash map
     */
    public HashMap<String, String> getHeaders();
    
    /**
     * Set the headers for this HTTP response
     * 
     * @param headers Headers in a key => value hash map
     * @return This response instance
     */
    public ResponseInterface setHeaders(HashMap<String, String> headers);
    
    /**
     * Get the response body
     * 
     * @return Response body
     */
    public String getBody();
    
    /**
     * Set the body contents
     * 
     * @param body The body of the request, as a string
     * @return This response instance
     */
    public ResponseInterface setBody(String body);
    
    /**
     * Get the status code for this request
     * 
     * @return The status code for this request
     */
    public int getStatusCode();
    
    /**
     * Set the status code
     * 
     * @param code The HTTP status code to set
     * @return The status code for this request
     */
    public ResponseInterface setStatusCode(int code);
    
    /**
     * Get the optional Imbo error code from the body
     * 
     * @return The internal error code from Imbo
     */
    public int getImboErrorCode();
    
    /**
     * Whether or not the response is a success (in the 2xx range)
     * 
     * @return True if the request was a success, false otherwise
     */
    public boolean isSuccess();
    
    /**
     * Whether or not the response is an error (> 4xx range)
     * 
     * @return True if the request resulted in an error, false otherwise
     */
    public boolean isError();
    
    /**
     * Returns the image identifier associated with the response
     * 
     * If the response does not contain any image identifier (for instance if the
     * request made was against the metadata resource) NULL will be returned.
     * 
     * @return 
     */
    public String getImageIdentifier();
	
}