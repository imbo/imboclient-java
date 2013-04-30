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
package org.imboproject.javaclient.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Various text utilities
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class TextUtils {

    /**
     * Join a set of tokens with a given delimiter
     *
     * @param delimiter Which delimiter (character(s)) to use for separating tokens
     * @param tokens Tokens to separate
     * @return String, with the delimiter between each token
     */
    public static String join(CharSequence delimiter, Iterable<String> tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (CharSequence value : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(value);
        }

        return sb.toString();
    }

    /**
     * Join a set of tokens with a given delimiter
     *
     * @param delimiter Which delimiter (character(s)) to use for separating tokens
     * @param tokens Tokens to separate
     * @return String, with the delimiter between each token
     */
    public static String join(CharSequence delimiter, Object[] tokens) {
        StringBuilder sb = new StringBuilder();
        boolean firstTime = true;
        for (Object value : tokens) {
            if (firstTime) {
                firstTime = false;
            } else {
                sb.append(delimiter);
            }
            sb.append(value);
        }

        return sb.toString();
    }

    /**
     * URL-encode the given input string
     *
     * @param value Input value
     * @return URL-encoded string
     */
    public static String urlEncode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8")
                      .replaceAll("\\+", "%20")
                      .replaceAll("\\%21", "!")
                      .replaceAll("\\%27", "'")
                      .replaceAll("\\%28", "(")
                      .replaceAll("\\%29", ")")
                      .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            // This really should not happen, but if it does, use the raw value
            return value;
        }
    }

    /**
     * Normalize color definition
     *
     * @param color Input color definition
     * @return Normalized color definition
     */
    public static String normalizeColor(String color) {
        return color.replace("#", "").toLowerCase();
    }

}
