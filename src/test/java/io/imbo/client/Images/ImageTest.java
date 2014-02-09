/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package io.imbo.client.Images;

import static org.junit.Assert.assertEquals;
import io.imbo.client.Images.Image;

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
