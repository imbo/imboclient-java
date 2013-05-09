/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package org.imboproject.javaclient.Url;

import org.imboproject.javaclient.util.Crypto;

/**
 * Access token implementation
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class AccessToken implements AccessTokenInterface {

    /**
     * {@inheritDoc}
     */
    public String generateToken(String url, String key) {
        return Crypto.hashHmacSha256(url, key);
    }

}