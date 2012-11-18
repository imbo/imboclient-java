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
package org.imboproject.javaclient.util;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;

/**
 * Tests for the TextUtils class
 * 
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class TextUtilsTest {

    /**
     * Should be able to join string arrays with a single character as delimiter
     */
    @Test
    public void testJoinStringArrayWithSingleCharacterAsDelimiter() {
        String[] names = { "Christer", "Espen", "André", "Mats" };
        assertEquals("Christer,Espen,André,Mats", TextUtils.join(",", names));
    }
    
    /**
     * Should be able to join string arrays with several characters as delimiter
     */
    @Test
    public void testJoinStringArrayWithSeveralCharactersAsDelimiter() {
        String[] names = { "Christer", "Espen", "André", "Mats" };
        assertEquals("Christer, Espen, André, Mats", TextUtils.join(", ", names));
    }

    /**
     * Should be able to join ArrayLists with a single character as delimiter
     */
    @Test
    public void testJoinArrayListWithSingleCharacterAsDelimiter() {
        ArrayList<String> names = new ArrayList<String>(Arrays.asList(
            "Christer", "Espen", "André", "Mats"
        ));
        
        assertEquals("Christer,Espen,André,Mats", TextUtils.join(",", names));
    }
    
    /**
     * Should be able to ArrayLists with several characters as delimiter
     */
    @Test
    public void testJoinArrayListWithSeveralCharactersAsDelimiter() {
        ArrayList<String> names = new ArrayList<String>(Arrays.asList(
            "Christer", "Espen", "André", "Mats"
        ));
        assertEquals("Christer, Espen, André, Mats", TextUtils.join(", ", names));
    }
    
    /**
     * Should be able to encode simple strings
     */
    @Test
    public void testEncodingDoesNothingOnSimpleStrings() {
        String input = "Hey there";
        assertEquals("Hey+there", TextUtils.urlEncode(input));
    }
    
    /**
     * Should be able to encode more complex strings
     */
    @Test
    public void testEncodingOnTransformationExample() {
        String input = "border:color=000000,width=1,height=1";
        assertEquals(
            "border%3Acolor%3D000000%2Cwidth%3D1%2Cheight%3D1",
            TextUtils.urlEncode(input)
        );
    }
    
    /**
     * Normalizing a color should strip the hash symbol
     */
    @Test
    public void testNormalizingStripsHashSymbol() {
        assertEquals(
            "bf1942",
            TextUtils.normalizeColor("#bf1942")
        );
    }
    
    /**
     * Normalizing a color should lowercase definition
     */
    @Test
    public void testNormalizingLowercases() {
        assertEquals(
            "bf2142",
            TextUtils.normalizeColor("#BF2142")
        );
    }
}
