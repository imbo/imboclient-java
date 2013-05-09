/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package org.imboproject.javaclient.Url;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import org.imboproject.javaclient.util.RegexMatcher;
import org.junit.Test;

/**
 * Tests the metadata URL class
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class MetadataTest {

    @Test
    public void testCanGenerateACompleteUrlIncludingAnAccessToken() {
        String url = (new MetadataUrl("http://imbo", "publicKey", "privateKey", "image")).getUrl();

        assertThat(url, startsWith("http://imbo/users/publicKey/images/image/meta.json"));
        assertThat(url, RegexMatcher.matches(".*accessToken=[a-f0-9]{64}$"));
    }

}
