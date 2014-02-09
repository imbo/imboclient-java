/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package io.imbo.client.Url;

import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import io.imbo.client.Url.UserUrl;
import io.imbo.client.util.RegexMatcher;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Tests the User URL class
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
@RunWith(Parameterized.class)
public class UserTest {

    private String host;
    private String pubKey;
    private String expectedValue;

    public UserTest(String host, String publicKey, String expected) {
        this.host = host;
        this.pubKey = publicKey;
        this.expectedValue = expected;
    }

    @Test
    public void testCanGenerateACompleteUrlIncludingAnAccessToken() {
        String url = (new UserUrl(host, pubKey, "privateKey")).getUrl();

        assertThat(url, startsWith(expectedValue));
        assertThat(url, RegexMatcher.matches(".*accessToken=[a-f0-9]{64}$"));
    }

    @Parameterized.Parameters
    public static List<Object[]> getUrlData() {
        return Arrays.asList(new Object[][] {
            { "http://imbo", "publicKey", "http://imbo/users/publicKey.json" },
            { "http://imbo:6081", "foobar", "http://imbo:6081/users/foobar.json" }
        });
    }

}