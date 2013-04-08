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
package org.imboproject.javaclient.Url;

import org.imboproject.javaclient.util.TextUtils;

/**
 * Metadata URL
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class MetadataUrl extends Url implements UrlInterface {

    /**
     * Image identifier for the image
     */
    private String imageIdentifier;

    /**
     * Class constructor
     *
     * {@inheritDoc}
     * @param imageIdentifier The image identifier to use in the URL
     */
    public MetadataUrl(String baseUrl, String publicKey, String privateKey, String imageIdentifier) {
        super(baseUrl, publicKey, privateKey);

        this.imageIdentifier = imageIdentifier;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getResourceUrl() {
        String[] parts = {
            baseUrl,
            "users",
            publicKey,
            "images",
            imageIdentifier,
            "meta.json"
        };
        return TextUtils.join("/", parts);
    }

}