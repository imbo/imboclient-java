/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package io.imbo.client.Url;

import static org.junit.Assert.assertEquals;
import io.imbo.client.Url.StatusUrl;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Tests status URL class
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
@RunWith(Parameterized.class)
public class StatusTest {

    private String host;
    private String expectedValue;

    public StatusTest(String host, String expected) {
        this.host = host;
        this.expectedValue = expected;
    }

    @Test
    public void testCanGenerateACompleteUrlThatDoesNotIncludeAnAccessToken() {
        assertEquals(expectedValue, (new StatusUrl(host)).toString());
    }

    @Parameterized.Parameters
    public static List<Object[]> getUrlData() {
        return Arrays.asList(new Object[][] {
            { "http://imbo", "http://imbo/status.json" },
            { "http://imbo:6081", "http://imbo:6081/status.json" },
            { "https://imbo:6081/prefix", "https://imbo:6081/prefix/status.json" }
        });
    }

}