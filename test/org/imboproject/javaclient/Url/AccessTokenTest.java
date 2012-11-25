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
