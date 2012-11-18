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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;

import org.imboproject.javaclient.util.RegexMatcher;
import org.imboproject.javaclient.util.TextUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Image URL tests
 * 
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
public class ImageTest {

    private Image url;
    final private String baseUrl = "http://host";
    final private String publicKey = "public";
    final private String privateKey = "41ebdff96ee9986119a5033f30d9a6c8";
    final private String imageIdentifier = "3aea3926533f3c7b87d5500789aa2a17";
    
    /**
     * Set up the image URL instance
     */
    @Before
    public void setUp() {
        url = new Image(
            baseUrl,
            publicKey,
            privateKey,
            imageIdentifier
        ); 
    }

    /**
     * Tear down the image URL instance
     */
    @After
    public void tearDown() {
        url = null;
    }

    /**
     * The image URL must be able to apply the border transformation with no custom parameters
     */
    @Test
    public void testCanApplyTheBorderTransformationUsingDefaultParameters() {
        assertSame(url, url.border(null));
        assertThat(url.getUrl(), containsString("?t[]=border%3Acolor%3D000000%2Cwidth%3D1%2Cheight%3D1"));
    }
    
    /**
     * The image URL must be able to apply the border transformation with custom parameters
     */
    @Test
    public void testCanApplyTheBorderTransformationUsingCustomParameters() {
        assertSame(url, url.border("#fff", 2, 3));
        assertThat(url.getUrl(), containsString("?t[]=border%3Acolor%3Dfff%2Cwidth%3D2%2Cheight%3D3"));
    }
    
    /**
     * The image URL must be able to apply the compress transformation with no custom parameters
     */
    @Test
    public void testCanApplyTheCompressTransformationUsingDefaultParameters() {
        assertSame(url, url.compress());
        assertThat(url.getUrl(), containsString("?t[]=compress%3Aquality%3D75"));
    }
    
    /**
     * The image URL must be able to apply the compress transformation with custom parameters
     */
    @Test
    public void testCanApplyTheCompressTransformationUsingCustomParameters() {
        assertSame(url, url.compress(42));
        assertThat(url.getUrl(), containsString("?t[]=compress%3Aquality%3D42"));
    }
    
    /**
     * The image URL must be able to apply the convert transformation using custom extensions
     */
    @Test
    public void testCanApplyTheConvertTransformationUsingACustomExtension() {
        assertSame(url, url.convert("bmp"));
        
        assertThat(url.getUrl(), startsWith(
            getExpectedResourceUrl() + ".bmp"
        ));
    }
    
    /**
     * The image URL must be able to apply the convert transformation using the gif() convenience method
     */
    @Test
    public void testCanApplyTheConvertTransformationUsingTheGifConvenienceMethod() {
        assertSame(url, url.gif());
        
        assertThat(url.getUrl(), startsWith(
            getExpectedResourceUrl() + ".gif"
        ));
    }
    
    /**
     * The image URL must be able to apply the convert transformation using the jpg() convenience method
     */
    @Test
    public void testCanApplyTheConvertTransformationUsingTheJpgConvenienceMethod() {
        assertSame(url, url.jpg());
        
        assertThat(url.getUrl(), startsWith(
            getExpectedResourceUrl() + ".jpg"
        ));
    }
    
    /**
     * The image URL must be able to apply the convert transformation using the png() convenience method
     */
    @Test
    public void testCanApplyTheConvertTransformationUsingThePngConvenienceMethod() {
        assertSame(url, url.png());
        
        assertThat(url.getUrl(), startsWith(
            getExpectedResourceUrl() + ".png"
        ));
    }
    
    /**
     * The image URL must be able to apply the crop transformation with parameters
     */
    @Test
    public void testCanApplyTheCropTransformationWithParameters() {
        assertSame(url, url.crop(1, 2, 3, 4));
        
        assertThat(url.getUrl(), startsWith(
            getExpectedResourceUrl() + "?t[]=" + 
            "crop%3Ax%3D1%2Cy%3D2%2Cwidth%3D3%2Cheight%3D4"
        ));
    }
    
    /**
     * The image URL must be able to apply the flipHorizontally transformation
     */
    @Test
    public void testCanApplyTheFliphorizontallyTransformation() {
        assertSame(url, url.flipHorizontally());
        assertThat(url.getUrl(), containsString("?t[]=flipHorizontally"));
    }
    
    /**
     * The image URL must be able to apply the flipVertically transformation
     */
    @Test
    public void testCanApplyTheFlipverticallyTransformation() {
        assertSame(url, url.flipVertically());
        assertThat(url.getUrl(), containsString("?t[]=flipVertically"));
    }
    
    /**
     * The image URL must be able to apply the transpose transformation
     */
    @Test
    public void testCanApplyTheTransposeTransformation() {
        assertSame(url, url.transpose());
        assertThat(url.getUrl(), containsString("?t[]=transpose"));
    }
    
    /**
     * The image URL must be able to apply the transverse transformation
     */
    @Test
    public void testCanApplyTheTransverseTransformation() {
        assertSame(url, url.transverse());
        assertThat(url.getUrl(), containsString("?t[]=transverse"));
    }
    
    /**
     * The image URL must be able to apply the desaturate transformation
     */
    @Test
    public void testCanApplyTheDesaturateTransformation() {
        assertSame(url, url.desaturate());
        assertThat(url.getUrl(), containsString("?t[]=desaturate"));
    }
    
    /**
     * The image URL must be able to apply the resize transformation using only width
     */
    @Test
    public void testCanApplyTheResizeTransformationUsingOnlyWidth() {
        assertSame(url, url.resize(100, 0));
        assertThat(url.getUrl(), containsString("?t[]=resize%3Awidth%3D100"));
    }
    
    /**
     * The image URL must be able to apply the resize transformation using only height
     */
    @Test
    public void testCanApplyTheResizeTransformationUsingOnlyHeight() {
        assertSame(url, url.resize(0, 200));
        assertThat(url.getUrl(), containsString("?t[]=resize%3Aheight%3D200"));
    }
    
    /**
     * The image URL must be able to apply the resize transformation using both width and height
     */
    @Test
    public void testCanApplyTheResizeTransformationUsingBothWidthAndHeight() {
        assertSame(url, url.resize(175, 275));
        assertThat(url.getUrl(), containsString("?t[]=resize%3Awidth%3D175%2Cheight%3D275"));
    }
    
    /**
     * The image URL must be able to apply the maxSize transformation using only width
     */
    @Test
    public void testCanApplyTheMaxSizeTransformationUsingOnlyWidth() {
        assertSame(url, url.maxSize(100, 0));
        assertThat(url.getUrl(), containsString("?t[]=maxSize%3Awidth%3D100"));
    }
    
    /**
     * The image URL must be able to apply the maxSize transformation using only height
     */
    @Test
    public void testCanApplyTheMaxSizeTransformationUsingOnlyHeight() {
        assertSame(url, url.maxSize(0, 200));
        assertThat(url.getUrl(), containsString("?t[]=maxSize%3Aheight%3D200"));
    }
    
    /**
     * The image URL must be able to apply the maxSize transformation using both width and height
     */
    @Test
    public void testCanApplyTheMaxSizeTransformationUsingBothWidthAndHeight() {
        assertSame(url, url.maxSize(175, 275));
        assertThat(url.getUrl(), containsString("?t[]=maxSize%3Awidth%3D175%2Cheight%3D275"));
    }
    
    /**
     * The image URL must be able to apply the rotate transformation using only angle
     */
    @Test
    public void testCanApplyTheRotateTransformationUsingOnlyAngle() {
        assertSame(url, url.rotate(42.76));
        assertThat(url.getUrl(), containsString("?t[]=rotate%3Aangle%3D42.76%2Cbg%3D000000"));
    }
    
    /**
     * The image URL must be able to apply the rotate transformation using angle and bg
     */
    @Test
    public void testCanApplyTheRotateTransformationUsingAngleAndBackground() {
        assertSame(url, url.rotate(42, "fff"));
        assertThat(url.getUrl(), containsString("?t[]=rotate%3Aangle%3D42%2Cbg%3Dfff"));
    }
    
    /**
     * The image URL must be able to apply the thumbnail transformation using the default parameters
     */
    @Test
    public void testCanApplyTheThumbnailTransformationUsingDefaultParameters() {
        assertSame(url, url.thumbnail());
        assertThat(url.getUrl(), containsString("?t[]=thumbnail%3Awidth%3D50%2Cheight%3D50%2Cfit%3Doutbound"));
    }
    
    /**
     * The image URL must be able to apply the thumbnail transformation using custom parameters
     */
    @Test
    public void testCanApplyTheThumbnailTransformationUsingCustomParameters() {
        assertSame(url, url.thumbnail(1, 2, "inset"));
        assertThat(url.getUrl(), containsString("?t[]=thumbnail%3Awidth%3D1%2Cheight%3D2%2Cfit%3Dinset"));
    }
    
    /**
     * The image URL must be able to apply the canvas transformation using only required parameters
     */
    @Test
    public void testCanApplyTheCanvasTransformationUsingOnlyRequiredParameters() {
        assertSame(url, url.canvas(100, 200));
        assertThat(url.getUrl(), containsString("?t[]=canvas%3Awidth%3D100%2Cheight%3D200"));
    }
    
    /**
     * The image URL must be able to apply the canvas transformation using all parameters
     */
    @Test
    public void testCanApplyTheCanvasTransformationUsingAllParameters() {
        assertSame(url, url.canvas(100, 200, "free", 10, 20, "000"));
        assertThat(url.getUrl(), containsString("?t[]=canvas%3Awidth%3D100%2Cheight%3D200%2Cmode%3Dfree%2Cx%3D10%2Cy%3D20%2Cbg%3D000"));
    }
    
    /**
     * The image URL must be able to generate a complete URL with an access token appended
     */
    @Test
    public void testCanGenerateACompleteUrlIncludingAnAccessToken() {
        assertThat(url.getUrl(), startsWith(getExpectedResourceUrl()));
        assertThat(url.getUrl(), RegexMatcher.matches(".*accessToken=[a-f0-9]{64}$"));
    }
    
    /**
     * The image URL must be able to reset all applied transformations
     */
    @Test
    public void testCanResetAllAppliedTransformations() {
        this.url.gif();
        this.url.flipHorizontally();
        assertThat(url.getUrl(), startsWith(getExpectedResourceUrl() + ".gif?t[]=flip"));
        
        this.url.reset();
        assertThat(url.getUrl(), startsWith(getExpectedResourceUrl() + "?accessToken="));
    }
    
    /**
     * The image URL must be able to chain all available transformations
     */
    @Test
    public void testCanChainAllTransformations() {
        url.border("000000")
           .compress()
           .convert("png")
           .crop(1, 1, 40, 40)
           .flipHorizontally()
           .flipVertically()
           .resize(200, 0)
           .maxSize(100, 0)
           .rotate(90)
           .thumbnail()
           .canvas(300, 300)
           .transpose()
           .transverse()
           .desaturate()
           .getUrl();
     
        assertThat(url.getUrl(), startsWith(
            getExpectedResourceUrl() + ".png?" +
            "t[]=" + TextUtils.urlEncode("border:color=000000,width=1,height=1") + "&" + 
            "t[]=" + TextUtils.urlEncode("compress:quality=75") + "&" + 
            "t[]=" + TextUtils.urlEncode("crop:x=1,y=1,width=40,height=40") + "&" + 
            "t[]=" + TextUtils.urlEncode("flipHorizontally") + "&" + 
            "t[]=" + TextUtils.urlEncode("flipVertically") + "&" + 
            "t[]=" + TextUtils.urlEncode("resize:width=200") + "&" + 
            "t[]=" + TextUtils.urlEncode("maxSize:width=100") + "&" + 
            "t[]=" + TextUtils.urlEncode("rotate:angle=90,bg=000000") + "&" + 
            "t[]=" + TextUtils.urlEncode("thumbnail:width=50,height=50,fit=outbound") + "&" + 
            "t[]=" + TextUtils.urlEncode("canvas:width=300,height=300") + "&" + 
            "t[]=" + TextUtils.urlEncode("transpose") + "&" + 
            "t[]=" + TextUtils.urlEncode("transverse") + "&" + 
            "t[]=" + TextUtils.urlEncode("desaturate") + "&" +
            "accessToken="
        ));
    }
    
    /**
     * Returns the expected resource URL (host/users/key/images/identifier)
     * 
     * @return Expected resource URL
     */
    private String getExpectedResourceUrl() {
        String[] parts = {
            baseUrl,
            "users",
            publicKey,
            "images",
            imageIdentifier
        };
        
        return TextUtils.join("/", parts);
    }

}
