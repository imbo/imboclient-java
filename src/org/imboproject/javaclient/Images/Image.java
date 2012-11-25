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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONObject;

/**
 * Image implementation
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class Image implements ImageInterface {

    private String identifier;
    private int size;
    private String extension;
    private String mimeType;
    private Date addedDate;
    private Date updatedDate;
    private int width;
    private int height;
    private String checksum;
    private String publicKey;

    public Image() {
        // Allow a raw state
    }

    /**
     * Creates a new Image instance from passed data
     *
     * @param data JSONObject containing the data for this image
     */
    public Image(JSONObject data) {
        populate(data);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIdentifier() {
        return identifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getExtension() {
        return extension;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMimeType() {
        return mimeType;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getAddedDate() {
        return addedDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date getUpdatedDate() {
        return updatedDate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getChecksum() {
        return checksum;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Set the date when the image was added
     *
     * @param added Date definition, in format: "EEE, d MMM yyyy HH:mm:ss z"
     */
    private void setAddedDate(String added) {
        addedDate = parseDate(added);
    }

    /**
     * Set the date when the image was last updated
     *
     * @param updated Date definition, in format: "EEE, d MMM yyyy HH:mm:ss z"
     */
    private void setUpdatedDate(String updated) {
        updatedDate = parseDate(updated);
    }

    /**
     * Parse a date in format: "EEE, d MMM yyyy HH:mm:ss z", converting it to a Date
     *
     * @param date Date definition, in format: "EEE, d MMM yyyy HH:mm:ss z"
     * @return Parsed date
     */
    private Date parseDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z");
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * Populate this instance with the passed data
     *
     * @param data JSONObject containing the data for this image
     */
    private void populate(JSONObject data) {
        identifier = data.optString("imageIdentifier", null);
        size       = data.optInt("size");
        extension  = data.optString("extension", null);
        mimeType   = data.optString("mime", null);
        width      = data.optInt("width");
        height     = data.optInt("height");
        checksum   = data.optString("checksum", null);
        publicKey  = data.optString("publicKey", null);

        setAddedDate(data.optString("added", ""));
        setUpdatedDate(data.optString("added", ""));
    }

}