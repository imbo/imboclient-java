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
package org.imboproject.javaclient.Images;

import static org.junit.Assert.assertEquals;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Image class test
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class ImageTest {

    private JSONObject data;
    private Image image;

    @Before
    public void setUp() throws JSONException {
        data = new JSONObject();
        data.put("imageIdentifier", "995b506ba1772e6a3fa25a2e3e618b08");
        data.put("size", 655114);
        data.put("extension", "png");
        data.put("mime", "image/png");
        data.put("added", "Thu, 15 Nov 2012 15:44:49 GMT");
        data.put("width", 640);
        data.put("height", 480);
        data.put("checksum", "995b506ba1772e6a3fa25a2e3e618b08");
        data.put("publicKey", "testsuite");
        data.put("updated", "Thu, 01 May 2013 11:49:48 CET");

        image = new Image(data);
    }

    @After
    public void tearDown() {
        image = null;
    }

    /**
     * The image instance must be able to fetch the image identifier
     */
    @Test
    public void testCanGetImageIdentifierAfterBeingPopulatedThroughConstructor() {
        assertEquals(data.optString("imageIdentifier"), image.getIdentifier());
    }

    /**
     * The image instance must be able to fetch the size
     */
    @Test
    public void testCanGetSizeAfterBeingPopulatedThroughConstructor() {
        assertEquals(data.optInt("size"), image.getSize());
    }

    /**
     * The image instance must be able to fetch the extension
     */
    @Test
    public void testCanGetExtensionAfterBeingPopulatedThroughConstructor() {
        assertEquals(data.optString("extension"), image.getExtension());
    }

    /**
     * The image instance must be able to fetch the mime type
     */
    @Test
    public void testCanGetMimeTypeAfterBeingPopulatedThroughConstructor() {
        assertEquals(data.optString("mime"), image.getMimeType());
    }

    /**
     * The image instance must be able to fetch the added date as a Date instance
     */
    @Test
    public void testCanGetAddedDateAsDateInstanceAfterBeingPopulatedThroughConstructorAsFormattedString() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
        dt.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        assertEquals("2012-11-15 03:44:49 UTC", dt.format(image.getAddedDate()));
    }

    /**
     * The image instance must be able to fetch the updated date as a Date instance
     */
    @Test
    public void testCanGetUpdatedDateAsDatetimeInstanceAfterBeingPopulatedThroughConstructorAsFormattedString() {
        SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss z");
        dt.setTimeZone(TimeZone.getTimeZone("UTC"));
        
        assertEquals("2013-05-01 10:49:48 UTC", dt.format(image.getUpdatedDate()));
    }

    /**
     * The image instance must be able to fetch the width
     */
    @Test
    public void testCanGetWidthAfterBeingPopulatedThroughConstructor() {
        assertEquals(data.optInt("width"), image.getWidth());
    }

    /**
     * The image instance must be able to fetch the height
     */
    @Test
    public void testCanGetHeightAfterBeingPopulatedThroughConstructor() {
        assertEquals(data.optInt("height"), image.getHeight());
    }

    /**
     * The image instance must be able to fetch the checksum
     */
    @Test
    public void testCanGetChecksumAfterBeingPopulatedThroughConstructor() {
        assertEquals(data.optString("checksum"), image.getChecksum());
    }

    /**
     * The image instance must be able to fetch the public key
     */
    @Test
    public void testCanGetPublicKeyAfterBeingPopulatedThroughConstructor() {
        assertEquals(data.optString("publicKey"), image.getPublicKey());
    }

}
