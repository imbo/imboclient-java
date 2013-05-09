/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package org.imboproject.javaclient.Images;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
    public String getIdentifier() {
        return identifier;
    }

    /**
     * {@inheritDoc}
     */
    public int getSize() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    public String getExtension() {
        return extension;
    }

    /**
     * {@inheritDoc}
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * {@inheritDoc}
     */
    public Date getAddedDate() {
        return addedDate;
    }

    /**
     * {@inheritDoc}
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    /**
     * {@inheritDoc}
     */
    public int getWidth() {
        return width;
    }

    /**
     * {@inheritDoc}
     */
    public int getHeight() {
        return height;
    }

    /**
     * {@inheritDoc}
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * {@inheritDoc}
     */
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
        DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss z", Locale.US);
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
        setUpdatedDate(data.optString("updated", ""));
    }

}