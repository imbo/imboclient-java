/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
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
