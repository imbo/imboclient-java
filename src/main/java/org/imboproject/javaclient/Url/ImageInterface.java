/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package org.imboproject.javaclient.Url;

import java.net.URI;

/**
 * Custom Interface for image URLs
 * 
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public interface ImageInterface {

    /**
     * Append a border transformation query parameter to the URL
     * 
     * @param color Color of the border, in hex format
     * @param width Width of the left and right sides of the border
     * @param height Height of the left and right sides of the border
     * @return ImageInterface
     */
    public ImageInterface border(String color, int width, int height);
    
    /**
     * Append a border transformation query parameter to the URL.
     * Border will be 1px in width and height. 
     * 
     * @param color Color of the border, in hex format. Defaults to '000000' (black)
     * @return ImageInterface
     */
    public ImageInterface border(String color);

    /**
     * Append a compress transformation query parameter to the URL
     *
     * @param quality A value between 0 and 100 where 100 is the best
     * @return ImageInterface
     */
    public ImageInterface compress(int quality);
    
    /**
     * Append a compress transformation query parameter to the URL with a value of 75
     * 
     * @return ImageInterface
     */
    public ImageInterface compress();

    /**
     * Change the URL to trigger the convert transformation
     *
     * @param type The type to convert to
     * @return ImageInterface
     */
    public ImageInterface convert(String type);

    /**
     * Convenience method to trigger GIF conversion
     *
     * @return ImageInterface
     */
    public ImageInterface gif();

    /**
     * Convenience method to trigger JPG conversion
     *
     * @return ImageInterface
     */
    public ImageInterface jpg();

    /**
     * Convenience method to trigger PNG conversion
     *
     * @return ImageInterface
     */
    public ImageInterface png();

    /**
     * Append a crop transformation query parameter to the URL
     *
     * @param x X coordinate of the top left corner of the crop
     * @param y Y coordinate of the top left corner of the crop
     * @param width Width of the crop
     * @param height Height of the crop
     * @return ImageInterface
     */
    public ImageInterface crop(int x, int y, int width, int height);

    /**
     * Append a flipHorizontally transformation query parameter to the URL
     *
     * @return ImageInterface
     */
    public ImageInterface flipHorizontally();

    /**
     * Append a flipVertically transformation query parameter to the URL
     *
     * @return ImageInterface
     */
    public ImageInterface flipVertically();

    /**
     * Append a resize transformation query parameter to the URL
     *
     * @param width Width of the resized image
     * @param height Height of the resized image
     * @return ImageInterface
     */
    public ImageInterface resize(int width, int height);

    /**
     * Append a maxSize transformation query parameter to the URL
     *
     * @param maxWidth Max width of the resized image
     * @param maxHeight Max height of the resized image
     * @return ImageInterface
     */
    public ImageInterface maxSize(int maxWidth, int maxHeight);

    /**
     * Append a rotate transformation query parameter to the URL
     *
     * @param angle The angle to rotate
     * @param bg Background color of the rotated image
     * @return ImageInterface
     */
    public ImageInterface rotate(double angle, String bg);
    
    /**
     * Append a rotate transformation query parameter to the URL
     *
     * @param angle The angle to rotate
     * @return ImageInterface
     */
    public ImageInterface rotate(double angle);

    /**
     * Append a thumbnail transformation query parameter to the URL
     *
     * @param width Width of the thumbnail
     * @param height Height of the thumbnail
     * @param fit Fit type. 'outbound' or 'inset'
     * @return ImageInterface
     */
    public ImageInterface thumbnail(int width, int height, String fit);
    
    /**
     * Append a thumbnail transformation query parameter to the URL
     *
     * @return ImageInterface
     */
    public ImageInterface thumbnail();

    /**
     * Append a canvas transformation query parameter to the URL
     *
     * @param width Width of the new canvas
     * @param height Height of the new canvas
     * @param mode The placement mode
     * @param x X coordinate of the placement of the upper left corner of the existing image
     * @param y Y coordinate of the placement of the upper left corner of the existing image
     * @param bg Background color of the canvas, in hex-format
     * @return ImageInterface
     */
    public ImageInterface canvas(int width, int height, String mode, int x, int y, String bg);
    
    /**
     * Append a canvas transformation query parameter to the URL
     *
     * @param width Width of the new canvas
     * @param height Height of the new canvas
     * @return ImageInterface
     */
    public ImageInterface canvas(int width, int height);

    /**
     * Append a transpose transformation query parameter to the URL
     *
     * @return ImageInterface
     */
    public ImageInterface transpose();

    /**
     * Append a transverse transformation query parameter to the URL
     *
     * @return ImageInterface
     */
    public ImageInterface transverse();

    /**
     * Append a desaturate transformation query parameter to the URL
     *
     * @return ImageInterface
     */
    public ImageInterface desaturate();
    
    /**
     * Returns the URL with query parameters added
     * 
     * @return Full URL with query parameters, as a String
     */
    public String getUrl();
    
    /**
     * Returns the URL as a URI with query parameters added
     * 
     * @return Full URL with query parameters, as a URI
     */
    public URI toUri();
}
