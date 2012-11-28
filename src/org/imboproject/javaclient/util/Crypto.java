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

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Cryptography tools
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class Crypto {

    /**
     * Hash a string of data using a given key with the HMAC-SHA256 algorithm
     *
     * @param data Input data
     * @param key Key to use for hashing
     * @return Hashed output
     */
    public static String hashHmacSha256(String data, String key) {
        Charset charset = Charset.forName("UTF-8");
        String algoName = "HmacSHA256";
        Mac algorithm = null;
        try {
            algorithm = Mac.getInstance(algoName);
        } catch (NoSuchAlgorithmException e) {
            // This should hopefully never happen
            return "hmac-sha-256-algorithm-not-found";
        }

        byte[] byteKey = charset.encode(key).array();
        SecretKeySpec secretKey = new javax.crypto.spec.SecretKeySpec(byteKey, algoName);
        try {
            algorithm.init(secretKey);
        } catch (InvalidKeyException e) {
            // .. And this shouldn't really ever happen, either
            return "invalid-key-for-access-token-generation";
        }

        final byte[] macData = algorithm.doFinal(charset.encode(data).array());

        String result = "";
        for (final byte element : macData) {
           result += Integer.toString((element & 0xff) + 0x100, 16).substring(1);
        }

        return result;
    }

}
