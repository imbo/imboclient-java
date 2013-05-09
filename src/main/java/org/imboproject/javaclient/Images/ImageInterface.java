/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package org.imboproject.javaclient.Images;

import java.util.Date;

/**
 * Interface for an image found in a response to an images query
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public interface ImageInterface {

    /**
     * Returns the image identifier for the image
     *
     * @return Image identifier
     */
    public String getIdentifier();

    /**
     * Returns the size of the image, in bytes
     *
     * @return Size of image, in bytes
     */
    public int getSize();

    /**
     * Returns the original extension for the image
     *
     * @return Extension (jpg, png, gif etc)
     */
    public String getExtension();

    /**
     * Returns the mime type of the image
     *
     * @return Mime type of image
     */
    public String getMimeType();

    /**
     * Returns the date on which the image was added
     *
     * @return Date on which the image was added
     */
    public Date getAddedDate();

    /**
     * Returns the date on which the image was last updated
     *
     * @return Date on which the image was last updated
     */
    public Date getUpdatedDate();

    /**
     * Returns the width of the image, in pixels
     *
     * @return Width of the image, in pixels
     */
    public int getWidth();

    /**
     * Returns the height of the image, in pixels
     *
     * @return Height of the image, in pixels
     */
    public int getHeight();

    /**
     * Returns an MD5 checksum of the image data
     *
     * @return MD5 checksum of image data
     */
    public String getChecksum();

    /**
     * Returns the public key in which the image is cataloged under
     *
     * @return Public key
     */
    public String getPublicKey();

}