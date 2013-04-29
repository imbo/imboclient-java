/**
 * ImboClient-java
 *
 * Copyright (c) 2013, Espen Hovlandsdal <espen@hovlandsdal.com>
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
 * @copyright Copyright (c) 2013, Espen Hovlandsdal <espen@hovlandsdal.com>
 * @license http://www.opensource.org/licenses/mit-license MIT License
 * @link https://github.com/rexxars/imboclient-java
 */
package org.imboproject.javaclient.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.apache.http.client.methods.HttpPut;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 */
public class PutBodyMatches extends TypeSafeMatcher<HttpPut> {

    private byte[] body;

    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("does not match expected body");
    }

    @Override
    protected boolean matchesSafely(HttpPut put) {
    	byte[] content = null;
    	try {
			content = readInputStream(put.getEntity().getContent());
		} catch (Exception e) {}
		
		return Arrays.equals(content, this.body);
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

    @Factory
    public static <T> Matcher<HttpPut> PutBodyMatches(byte[] body) {
        PutBodyMatches matcher = new PutBodyMatches();
        matcher.setBody(body);

        return matcher;
    }

}