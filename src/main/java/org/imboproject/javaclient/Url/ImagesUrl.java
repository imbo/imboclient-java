/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package org.imboproject.javaclient.Url;

import org.imboproject.javaclient.util.TextUtils;

/**
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 */
public class ImagesUrl extends Url implements UrlInterface {

    public ImagesUrl(String baseUrl, String publicKey, String privateKey) {
        super(baseUrl, publicKey, privateKey);
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
            "images.json"
        };
        return TextUtils.join("/", parts);
    }

}