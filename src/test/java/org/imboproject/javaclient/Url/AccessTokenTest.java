/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package org.imboproject.javaclient.Url;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Tests the AccessToken class
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
@RunWith(Parameterized.class)
public class AccessTokenTest {

    /**
     * Used for the data provider
     */
    private String key1;
    private String key2;

    private AccessTokenInterface accessToken;

    public AccessTokenTest(String key1, String key2) {
        this.key1 = key1;
        this.key2 = key2;
    }

    /**
     * Set up the access token instance
     */
    @Before
    public void setUp() {
        accessToken = new AccessToken();
    }

    /**
     * Tear down the access token instance
     */
    @After
    public void tearDown() {
        accessToken = null;
    }

    /**
     * The access token must generate the same token every time given the same URL and key
     */
    @Test
    public void testWillGenerateTheSameKeyEveryTimeGivenTheSameUrlAndKey() {
        String url = "http://imbo/users/user/images.json";
        String key = "some key";

        assertEquals(
            accessToken.generateToken(url, key),
            accessToken.generateToken(url, key)
        );
    }

    /**
     * The access token must generate different tokens given different keys
     */
    @Test
    public void testWillGenerateDifferentTokensGivenDifferentKeys() {
        String url = "http://imbo/users/user/images.json";

        assertThat(
            accessToken.generateToken(url, this.key1),
            not(accessToken.generateToken(url, this.key2))
        );
    }

    @Parameterized.Parameters
    public static List<Object[]> getKeys() {
        return Arrays.asList(new Object[][] {
            { "key1", "key2" },
            { "key2", "key3" },
            { "key3", "key4" },
            { "key4", "key5" },
            { "key5", "key6" },
            { "key6", "key7" },
            { "key7", "key8" },
            { "key8", "key9" }
        });
    }

}
