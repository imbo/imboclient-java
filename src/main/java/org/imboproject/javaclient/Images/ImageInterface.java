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