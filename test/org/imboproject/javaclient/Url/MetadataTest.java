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
        String url = (new Metadata("http://imbo", "publicKey", "privateKey", "image")).getUrl();

        assertThat(url, startsWith("http://imbo/users/publicKey/images/image/meta.json"));
        assertThat(url, RegexMatcher.matches(".*accessToken=[a-f0-9]{64}$"));
    }

}
