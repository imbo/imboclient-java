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
package org.imboproject.javaclient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.startsWith;
import static org.imboproject.javaclient.util.UriMatches.UriMatches;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;

import junit.framework.TestCase;

import org.imboproject.javaclient.Http.Response;
import org.imboproject.javaclient.Http.ResponseInterface;
import org.imboproject.javaclient.Images.Image;
import org.imboproject.javaclient.Images.QueryInterface;
import org.imboproject.javaclient.Url.ImageUrl;
import org.imboproject.javaclient.Url.ImagesUrl;
import org.imboproject.javaclient.Url.MetadataUrl;
import org.imboproject.javaclient.Url.StatusUrl;
import org.imboproject.javaclient.Url.UserUrl;
import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Client test
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
@RunWith(JUnit4.class)
public class ClientTest extends TestCase {

	@Rule
    public JUnitRuleMockery context = new JUnitRuleMockery();

    @Rule
    public ExpectedException exception = ExpectedException.none();

    public final org.imboproject.javaclient.Http.ClientInterface httpClient = context.mock(org.imboproject.javaclient.Http.ClientInterface.class);

    private int mockCount = 0;
    private Client client;
    private String serverUrl = "http://host";
    private String publicKey = "key";
    private String privateKey = "8495c97ea3a313c12c0661dc5526e769";
    private String imageIdentifier = "23d7f91b25f3013fcc75ce070c40e004";
    private String signedUrlPattern = ".*?signature=.*?&timestamp=\\d{4}-\\d\\d-\\d\\dT\\d\\d%3A\\d\\d%3A\\d\\dZ$";

    @Before
    public void setUp() {
        // Set up Imbo client
        client = new Client(serverUrl, publicKey, privateKey);
        client.setHttpClient(httpClient);
    }

    @After
    public void tearDown() {
        client = null;
    }

    /**
     * When trying to add a local image that does not exist the client must throw an exception
     */
    @Test
    public void testThrowsExceptionWhenTryingToAddLocalImageThatDoesNotExist() throws IOException {
        exception.expect(FileNotFoundException.class);
        exception.expectMessage("The system cannot find the file specified");

        File nonExistant = new File("non-existant-file");
        client.addImage(nonExistant);
    }

    /**
     * When trying to add an empty local image the client must throw an exception
     */
    @Test
    public void testThrowsExceptionWhenTryingToAddEmptyLocalImage() throws IOException {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The specified file was empty");

        File emptyImage = new File("misc/empty.jpg");
        client.addImage(emptyImage);
    }

    /**
     * The client must be able to add a valid local image
     */
    @Test
    public void testReturnsResponseWhenAddingValidLocalImage() throws IOException {
    	final File image = new File("misc/imbo-logo.png");
    	final Response response = new Response();

    	context.checking(new Expectations() {{
    	    oneOf(httpClient).put(with(uriMatches(signedUrlPattern)), with(same(image)));
    	    will(returnValue(response));
        }});

		assertSame(response, client.addImage(image));
    }
    
    /**
     * The client must be able to add an in-memory image
     */
    @Test
    public void testReturnsResponseWhenAddingInMemoryImage() throws IOException {
    	final byte[] imgBytes = { 1, 2, 3, 4, 5 };
    	final Response response = new Response();
    	
    	context.checking(new Expectations() {{
    		oneOf(httpClient).put(with(uriMatches(signedUrlPattern)), with(any(ByteArrayInputStream.class)));
    		will(returnValue(response));
    	}});
    	
    	assertSame(response, client.addImage(imgBytes));
    }
    
    /**
     * The client must throw an exception when trying to add an empty in-memory image
     */
    @Test
    public void testThrowsExceptionWhenTryingToAddEmptyInMemoryImage() throws IOException {
    	final byte[] imgBytes = { };
    	
    	exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Byte array is empty");

        client.addImage(imgBytes);
    }

    /**
     * The client must be able to fetch an image from an URL and add it
     */
    @Test
    public void testReturnsResponseWhenAddingARemoteImage() throws URISyntaxException, IOException {
        final ResponseInterface getResponse = getResponseMock();
        final Response putResponse = new Response();
        final URI imageUrl = new URI("http://example.com/image.jpg");
        final byte[] imgBytes = { 1, 2, 3, 4, 5 };

        context.checking(new Expectations() {{
            oneOf(httpClient).get(with(same(imageUrl)));
            will(returnValue(getResponse));

            oneOf(getResponse).getRawBody();
            will(returnValue(imgBytes));

            oneOf(httpClient).put(with(uriMatches(signedUrlPattern)), with(any(ByteArrayInputStream.class)));
            will(returnValue(putResponse));
        }});

        assertSame(putResponse, client.addImageFromUrl(imageUrl));
    }
    
    /**
     * The client must throw an exception when trying to add an empty remote image
     */
    @Test
    public void testThrowsExceptionWhenTryingToAddEmptyRemoteImage() throws URISyntaxException, IOException {
    	final byte[] imgBytes = { };
    	final ResponseInterface getResponse = getResponseMock();
    	final ResponseInterface putResponse = getResponseMock();
    	final URI url = new URI("http://example.com/image.jpg");
    	
    	context.checking(new Expectations() {{
            oneOf(httpClient).get(with(same(url)));
            will(returnValue(getResponse));

            oneOf(getResponse).getRawBody();
            will(returnValue(imgBytes));

            oneOf(httpClient).put(with(uriMatches(signedUrlPattern)), with(any(ByteArrayInputStream.class)));
            will(returnValue(putResponse));
        }});
    	
    	exception.expect(IllegalArgumentException.class);
        exception.expectMessage("Byte array is empty");

        client.addImageFromUrl(url);
    }
    
    /**
     * The client must return a valid response after deleting a remote image
     */
    @Test
    public void testReturnsResponseAfterDeletingAnImage() throws IOException {
    	final ResponseInterface response = getResponseMock();
    	
    	context.checking(new Expectations() {{
            oneOf(httpClient).delete(with(any(URI.class)));
            will(returnValue(response));
        }});
    	
    	assertSame(response, client.deleteImage(imageIdentifier));
    }
    
    /**
     * The client must return a valid response after editing metadata
     * 
     * @throws IOException
     * @throws JSONException 
     */
    @Test
    public void testReturnsResponseAfterEditingMetadata() throws IOException, JSONException {
        final String metadata = "{\"foo\":\"bar\",\"bar\":\"foo\"}";
        
        final ResponseInterface response = getResponseMock();
        context.checking(new Expectations() {{
        	oneOf(httpClient).post(with(uriMatches(signedUrlPattern)), with(same(metadata)));
        	will(returnValue(response));
        }});

        assertSame(response, this.client.editMetadata(this.imageIdentifier, new JSONObject(metadata)));
    }

    /**
     * The client must return a valid response after having replaced metadata
     * 
     * @throws IOException 
     * @throws JSONException 
     */
    @Test
    public void testReturnsResponseAfterReplacingMetadata() throws IOException, JSONException {
    	final String metadata = "{\"foo\":\"bar\",\"bar\":\"foo\"}";
        
        final ResponseInterface response = getResponseMock();
        context.checking(new Expectations() {{
        	oneOf(httpClient).post(with(uriMatches(signedUrlPattern)), with(same(metadata)));
        	will(returnValue(response));
        }});

        assertSame(response, this.client.replaceMetadata(this.imageIdentifier, new JSONObject(metadata)));
    }

    /**
     * The client must return a valid response after having deleted metadata
     * 
     * @throws IOException 
     */
    @Test
    public void testReturnsResponseAfterDeletingMetadata() throws IOException {
        final ResponseInterface response = getResponseMock();
        
        context.checking(new Expectations() {{
        	oneOf(httpClient).delete(with(uriMatches(signedUrlPattern)));
        	will(returnValue(response));
        }});

        assertSame(response, this.client.deleteMetadata(this.imageIdentifier));
    }

    /**
     * The client must return metadata as an array when fetching metadata
     * 
     * @throws IOException 
     * @throws JSONException 
     */
    @Test
    public void testReturnsArrayWithMetadataWhenFetchingMetadata() throws IOException, JSONException {
        final String responseBody = "{\"foo\":\"bar\"}";

        final ResponseInterface response = getResponseMock();
        context.checking(new Expectations() {{
        	oneOf(response).getBody();
        	will(returnValue(responseBody));
        }});
        
        context.checking(new Expectations() {{
        	oneOf(httpClient).get(with(any(URI.class)));
        	will(returnValue(response));
        }});

        assertSame(
        	new JSONObject(responseBody),
            this.client.getMetadata(this.imageIdentifier)
        );
    }

    /**
     * The client must return a valid response after requesting an image using HEAD
     * 
     * @throws IOException 
     */
    @Test
    public void testReturnsResponseAfterRequestingImageUsingHttpHead() throws IOException {
        final ResponseInterface response = getResponseMock();

        context.checking(new Expectations() {{
        	oneOf(httpClient).head(with(any(URI.class)));
        	will(returnValue(response));
        }});

        assertSame(response, this.client.headImage(this.imageIdentifier));
    }

    /**
     * The client must throw an exception when checking if an image exists using a local image that
     * does not exist
     */
    @Test
    public void testThrowsExceptionWhenCheckingIfAnImageExistsUsingALocalImageThatDoesNotExist() throws IOException {
    	exception.expect(FileNotFoundException.class);
        exception.expectMessage("The system cannot find the file specified");
        
        File nonExistant = new File("non-existant.jpg");
    	this.client.imageExists(nonExistant);
    }

    /**
     * The client must throw an exception when checking if an image exists using an empty local
     * image
     */
    @Test
    public void testThrowsExceptionWhenCheckingIfAnImageExistsUsingAnEmptyLocalImage() throws IOException {
    	exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The specified file was empty");

        File emptyImage = new File("misc/empty.jpg");
        this.client.imageExists(emptyImage);
    }

    /**
     * If the server responds with an error other than 404 the client must re-throw the exception
     * 
     * @throws IOException 
     */
    @Test
    public void testThrowsExceptionWhenCheckingIfAnImageExistsAndTheServerRespondsWithAnErrorOtherThan404() throws IOException {
        File image = new File("misc/imbo-logo.png");
        
        exception.expect(ServerException.class);
        exception.expectMessage("Internal Server Error");
        
        context.checking(new Expectations() {{
        	oneOf(httpClient).head(with(any(URI.class)));
        	will(throwException(new ServerException("Internal Server Error", 500)));
        }});

        this.client.imageExists(image);
    }

    /**
     * When checking if an image identifier exists on the server and it does not,
     * the client must return false
     * 
     * @throws IOException 
     */
    @Test
    public void testReturnsFalseWhenCheckingIfARemoteImageIdentifierExistsAndTheImageDoesNotExist() throws IOException {
        context.checking(new Expectations() {{
        	oneOf(httpClient).head(with(any(URI.class)));
        	will(throwException(new ServerException("Image does not exist", 404)));
        }});

        assertFalse(this.client.imageExists(this.imageIdentifier));
    }

    /**
     * When checking if an image identifier exists on the server and it does,
     * the client must return true
     * 
     * @throws IOException 
     */
    @Test
    public void testReturnsTrueWhenCheckingIfARemoteImageIdentifierExistsAndTheImageExists() throws IOException {
        final ResponseInterface response = getResponseMock();

        context.checking(new Expectations() {{
        	oneOf(httpClient).head(with(any(URI.class)));
        	will(returnValue(response));
        }});

        assertTrue(this.client.imageExists(this.imageIdentifier));
    }

    /**
     * If the server responds with an error other than 404 the client must re-throw the exception
     * 
     * @throws IOException 
     */
    @Test
    public void testThrowsExceptionWhenCheckingIfAnImageIdentifierExistsAndTheServerRespondsWithAnErrorOtherThan404() throws IOException {
    	exception.expect(ServerException.class);
        exception.expectMessage("Internal Server Error");
        
        context.checking(new Expectations() {{
        	oneOf(httpClient).head(with(any(URI.class)));
        	will(throwException(new ServerException("Internal Server Error", 500)));
        }});

        this.client.imageExists(this.imageIdentifier);
    }

    /**
     * The client must be able to return an Url.Image instance based on an image
     * identifier
     */
    @Test
    public void testCanGenerateAnImageUrlInstanceBasedOnAnImageIdentifier() {
    	assertThat(
			this.client.getImageUrl(this.imageIdentifier),
			instanceOf(ImageUrl.class)
		);
    }

    /**
     * The client must be able to return an Url.Metadata instance based on an image
     * identifier
     */
    @Test
    public void testCanGenerateAMetadataUrlInstanceBasedOnAnImageIdentifier() {
    	assertThat(
			this.client.getMetadata(this.imageIdentifier),
			instanceOf(MetadataUrl.class)
		);
    }

    /**
     * The client must be able to return an Url.User instance based on the info given in
     * the constructor
     */
    @Test
    public void testCanGenerateAnUserUrlInstanceBasedOnParametersToConstructor() {
    	assertThat(
			this.client.getUserUrl(),
			instanceOf(UserUrl.class)
		);
    }

    /**
     * The client must be able to return an Url.Status instance based on the info given
     * in the constructor
     */
    @Test
    public void testCanGenerateAStatusUrlInstanceBasedOnParametersToConstructor() {
    	assertThat(
			this.client.getStatusUrl(),
			instanceOf(StatusUrl.class)
		);
    }

    /**
     * The client must be able to return an Url.Images instance based on the info given
     * in the constructor
     */
    @Test
    public void testCanGenerateAnImagesUrlInstanceBasedOnParametersToConstructor() {
    	assertThat(
			this.client.getImagesUrl(),
			instanceOf(ImagesUrl.class)
		);
    }

    /**
     * The client always selects the first URL in the set when generating an Url.Images
     * instance
     */
    @Test
    public void testSelectsTheFirstUrlInTheSetWhenGeneratingAStatusUrlInstance() {
    	String[] hosts = new String[] {
            "http://imbo1",
            "http://imbo2",
            "http://imbo3"
    	};
    	
        Client client = new Client(hosts, this.publicKey, this.privateKey);
        StatusUrl url = client.getStatusUrl();
        
        assertThat(
    		url.toString(),
    		startsWith("http://imbo1")
		);
    }

    /**
     * The client always selects the first URL in the set when generating an Url.Images
     * instance
     */
    @Test
    public void testSelectsTheFirstUrlInTheSetWhenGeneratingAnUserUrlInstance() {
    	String[] hosts = new String[] {
            "http://imbo1",
            "http://imbo2",
            "http://imbo3"
    	};
    	
    	Client client = new Client(hosts, this.publicKey, this.privateKey);
        UserUrl url = client.getUserUrl();
        
        assertThat(
    		url.toString(),
    		startsWith("http://imbo1")
		);
    }

    /**
     * The client always selects the first URL in the set when generating an Url.Images
     * instance
     */
    @Test
    public void testSelectsTheFirstUrlInTheSetWhenGeneratingAnImagesUrlInstance() {
    	String[] hosts = new String[] {
            "http://imbo1",
            "http://imbo2",
            "http://imbo3"
    	};
    	
    	Client client = new Client(hosts, this.publicKey, this.privateKey);
        ImagesUrl url = client.getImagesUrl();
        
        assertThat(
    		url.toString(),
    		startsWith("http://imbo1")
		);
    }

    /**
     * The client must be able to pick the same host from a set of hosts based on an image
     * identifier every time
     */
    @Test
    public void testSelectsTheSameHostFromASetOfHostsBasedOnAnImageIdentifierEveryTime() {
    	String[] hosts = new String[] {
            "http://imbo0",
            "http://imbo1/prefix",
            "http://imbo2:81",
            "http://imbo3:81/prefix",
            "http://imbo4:80"
        };
    	
    	String[] imageIdentifiers = new String[] {
			"d1afdbe2950dc1e9fa134d8c91cd1a8b",
            "5fda26a928c9b0b90ef7b2db0031bfcf",
            "5d028794b32c2b127875a336b1220dab",
            "f7dc62518f2967dacbc4c0eead5fabe5",
            "7a4cac9e82c06010293cd6d23708e147",
            "609c8d8350d3b6b294a628835b8e9b59",
            "1e68c888fbe0a27276141a1e6fb576f4",
            "67e45db3a472a90a26bda000c0818bfc",
            "3ad35117949c5a17b9df82c343b4f763"
    	};
    	
    	String[] expectedHosts = new String[] {
			"http://imbo4",
            "http://imbo0",
            "http://imbo3:81/prefix",
            "http://imbo2:81",
            "http://imbo2:81",
            "http://imbo1/prefix",
            "http://imbo0",
            "http://imbo3:81/prefix",
            "http://imbo3:81/prefix"
    	};
    	
    	Client client = new Client(hosts, this.publicKey, this.privateKey);
    	ImageUrl url;
    	
    	for (int i = 0; i < imageIdentifiers.length; i++) {
    		url = client.getImageUrl(imageIdentifiers[i]);
    		
    		assertThat(
	    		url.toString(),
	    		startsWith(expectedHosts[i])
			);
    	}
    }

    /**
     * The client must be able to return the number of images a user has stored remotely
     * 
     * @throws IOException 
     */
    @Test
    public void testCanFetchNumberOfImagesAUserHas() throws IOException {
        final ResponseInterface response = getResponseMock();
        
        context.checking(new Expectations() {{
        	oneOf(response).getBody();
        	will(returnValue("{\"numImage\":42}"));
        }});
        
        context.checking(new Expectations() {{
        	oneOf(httpClient).get(with(any(URI.class)));
        	will(returnValue(response));
        }});

        assertSame(42, this.client.getNumberOfImages());
    }

    /**
     * Returns example JSON-data for the images endpoint
     * 
     * @return String of JSON data
     */
	private String getImagesResponse() {
		return "[{\"size\":45826,\"publicKey\":\"christer\",\"imageIdentifier\":\"52116c74f6fba61bbc30c225d292d647\",\"extension\":\"png\",\"mime\":\"image\\/png\",\"added\":\"Thu, 27 Sep 2012 10:12:34 GMT\",\"updated\":\"Thu, 27 Sep 2012 10:12:34 GMT\",\"width\":380,\"height\":390,\"checksum\":\"52116c74f6fba61bbc30c225d292d647\"},{\"size\":10904,\"publicKey\":\"christer\",\"imageIdentifier\":\"3aae75c6085a7099114af8018e24c1cc\",\"extension\":\"png\",\"mime\":\"image\\/png\",\"added\":\"Thu, 27 Sep 2012 10:12:34 GMT\",\"updated\":\"Thu, 27 Sep 2012 10:12:34 GMT\",\"width\":210,\"height\":208,\"checksum\":\"3aae75c6085a7099114af8018e24c1cc\"},{\"size\":51424,\"publicKey\":\"christer\",\"imageIdentifier\":\"00f1e89ddfab8adb3ec018248bb96f5b\",\"extension\":\"jpg\",\"mime\":\"image\\/jpeg\",\"added\":\"Thu, 27 Sep 2012 10:12:34 GMT\",\"updated\":\"Thu, 27 Sep 2012 10:12:34 GMT\",\"width\":334,\"height\":500,\"checksum\":\"00f1e89ddfab8adb3ec018248bb96f5b\"},{\"size\":98457,\"publicKey\":\"christer\",\"imageIdentifier\":\"1ac50f402c1bf884483d8e42166edbbd\",\"extension\":\"jpg\",\"mime\":\"image\\/jpeg\",\"added\":\"Thu, 27 Sep 2012 10:12:34 GMT\",\"updated\":\"Thu, 27 Sep 2012 10:12:34 GMT\",\"width\":640,\"height\":480,\"checksum\":\"1ac50f402c1bf884483d8e42166edbbd\"},{\"size\":99899,\"publicKey\":\"christer\",\"imageIdentifier\":\"13e9bdd2b8f6b95d53ba5f4b66ecf2dc\",\"extension\":\"jpg\",\"mime\":\"image\\/jpeg\",\"added\":\"Thu, 27 Sep 2012 10:12:34 GMT\",\"updated\":\"Thu, 27 Sep 2012 10:12:34 GMT\",\"width\":640,\"height\":480,\"checksum\":\"13e9bdd2b8f6b95d53ba5f4b66ecf2dc\"}]";
	}
    
    /**
     * The client must be able to fetch images using no particular query
     * 
     * @throws IOException 
     */
    @Test
    public void testCanFetchImagesWithoutUsingAQueryObject() throws IOException {
        final ResponseInterface response = getResponseMock();
        context.checking(new Expectations() {{
        	oneOf(response).getBody();
        	will(returnValue(getImagesResponse()));
        }});
        
        context.checking(new Expectations() {{
        	oneOf(httpClient).get(with(any(URI.class)));
        	will(returnValue(response));
        }});

        Image[] images = this.client.getImages();
        
        for (int i = 0; i < images.length; i++) {
        	assertThat(
    			images[i],
    			instanceOf(Image.class)
    		);
        }
    }

    /**
     * The client must be able to fetch images using a query object
     * 
     * @throws IOException 
     * @throws JSONException 
     */
    @Test
    public void testCanFetchImagesUsingAQueryObject() throws IOException, JSONException {
        final ResponseInterface response = getResponseMock();
        final QueryInterface query = getQueryMock(); 
        
        context.checking(new Expectations() {{
        	oneOf(response).getBody();
        	will(returnValue(getImagesResponse()));
        }});
        
        context.checking(new Expectations() {{
        	oneOf(httpClient).get(with(uriMatches(".*?page=3&limit=5&query=%7B%22foo%22%3A%22bar%22%7D.*")));
        	will(returnValue(response));
        }});
        
        context.checking(new Expectations() {{
        	oneOf(query).page();
        	will(returnValue(3));
        	
        	oneOf(query).limit();
        	will(returnValue(5));
        	
        	oneOf(query).returnMetadata();
        	will(returnValue(false));
        	
        	oneOf(query).from();
        	will(returnValue(null));
        	
        	oneOf(query).to();
        	will(returnValue(null));
        	
        	oneOf(query).metadataQuery();
        	will(returnValue(new JSONObject("{\"foo\":\"bar\"}")));
        }});
        
        Image[] images = this.client.getImages(query);
        for (int i = 0; i < images.length; i++) {
        	assertThat(
    			images[i],
    			instanceOf(Image.class)
    		);
        }
    }

    /**
     * The client must be able to return the binary image data from a remote image using an image identifier
     * 
     * @throws IOException 
     */
    @Test
    public void testCanFetchBinaryImageDataBasedOnAnImageIdentifier() throws IOException {
        final byte[] expectedData = new byte[] { 1, 2, 3, 4, 5 };
        final ResponseInterface response = getResponseMock();
        
        context.checking(new Expectations() {{
        	oneOf(response).getRawBody();
        	will(returnValue(expectedData));
        }});
        
        context.checking(new Expectations() {{
        	oneOf(httpClient).get(with(any(URI.class)));
        	will(returnValue(response));
        }});

        assertSame(expectedData, this.client.getImageData(this.imageIdentifier));
    }
    
    /**
     * The client must be able to return the binary image data from a remote image using an URI
     * 
     * @throws IOException 
     */
    @Test
    public void testCanFetchBinaryImageDataBasedOnUri() throws IOException {
        final byte[] expectedData = new byte[] { 1, 2, 3, 4, 5 };
        final ResponseInterface response = getResponseMock();
        
        context.checking(new Expectations() {{
        	oneOf(response).getRawBody();
        	will(returnValue(expectedData));
        }});
        
        context.checking(new Expectations() {{
        	oneOf(httpClient).get(with(any(URI.class)));
        	will(returnValue(response));
        }});

    	ImageUrl url = this.client.getImageUrl(this.imageIdentifier);
        assertSame(expectedData, this.client.getImageData(
        	url.toUri()
        ));
    }

    /**
     * The client must be able to parse different URLs
     */
    @Test
    public void testAcceptsDifferentTypesOfHostUrlsInTheConstructor() {
        HashMap<String, String> urls = new HashMap<String, String>();
        urls.put("http://imbo",             "http://imbo");
        urls.put("http://imbo/prefix",      "http://imbo/prefix");
        urls.put("http://imbo:81",          "http://imbo:81");
        urls.put("http://imbo:81/prefix",   "http://imbo:81/prefix");
        urls.put("http://imbo:80",          "http://imbo");
        urls.put("http://imbo:80/prefix",   "http://imbo/prefix");
        urls.put("https://imbo",            "https://imbo");
        urls.put("https://imbo/prefix",     "https://imbo/prefix");
        urls.put("https://imbo:444",        "https://imbo:444");
        urls.put("https://imbo:444/prefix", "https://imbo:444/prefix");
        urls.put("https://imbo:443",        "https://imbo");
        urls.put("https://imbo:443/prefix", "https://imbo/prefix");
        
    	Client client;
    	String input;
    	String[] parsedUrls;
    	Iterator<String> keyIterator = urls.keySet().iterator();
    	while (keyIterator.hasNext()) {
    		input = keyIterator.next();
    		client = new Client(input, "publicKey", "privateKey");
    		parsedUrls = client.getServerUrls();
    		
    		assertEquals(1, parsedUrls.length);
    		assertSame(urls.get(input), parsedUrls[0]);
    	}
    }

    /**
     * The client must be able to return image properties of an existing remote image
     * 
     * @throws IOException 
     */
    @Test
    public void testCanReturnImagePropertiesOfAnExistingImage() throws IOException {
        String imageIdentifier = "8f552ba2a350be7ac19399365a738202";
        final HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-imbo-originalwidth",     "200");
        headers.put("x-imbo-originalheight",    "100");
        headers.put("x-imbo-originalfilesize",  "400");
        headers.put("x-imbo-originalmimetype",  "image/png");
        headers.put("x-imbo-originalextension", "png");
        
        
        final ResponseInterface response = getResponseMock();
        context.checking(new Expectations() {{
        	oneOf(response).getHeaders();
        	will(returnValue(headers));
        }});
        
        context.checking(new Expectations() {{
        	oneOf(httpClient).head(with(any(URI.class)));
        	will(returnValue(response));
        }});
        
        Image image = this.client.getImageProperties(imageIdentifier);
        assertSame(200,         image.getWidth());
        assertSame(100,         image.getHeight());
        assertSame(400,         image.getSize());
        assertSame("image/png", image.getMimeType());
        assertSame("png",       image.getExtension());
    }

    /**
     * The client must be able to generate an image identifier using a valid local image
     * 
     * @throws IOException 
     */
    @Test
    public void testCanGenerateAnImageIdentifierBasedOnAValidLocalImage() throws IOException {
        assertSame(
			"f9137fdccf9694912f3331e1f96ea72f",
			this.client.getImageIdentifier(new File("misc/imbo-logo.png"))
		);
        
        assertSame(
			"0f88d0234601c80a1ba634e0630fb11a",
			this.client.getImageIdentifier(new File("misc/imbo-logo.jpg"))
		);
        
        assertSame(
			"005004a9cca74dff1a7b54599abb2618",
			this.client.getImageIdentifier(new File("misc/imbo-logo.gif"))
		);
    }

    /**
     * The client must throw an exception when trying to generate an image identifier based on a
     * non-existing local image
     */
    @Test
    public void testThrowsExceptionWhenTryingToGenerateImageIdentifierBasedOnALocalImageThatDoesNotExist() throws IOException {
    	exception.expect(FileNotFoundException.class);
        exception.expectMessage("The system cannot find the file specified");
    	
    	this.client.getImageIdentifier(new File("foobar"));
    }

    /**
     * The client must throw an exception when trying to generate an image identifier based on an
     * empty local image
     * 
     * @throws IOException 
     */
    @Test
    public void testThrowsExceptionWhenTryingToGenerateImageIdentifierBasedOnAnEmptyLocalImage() throws IllegalArgumentException, IOException {
    	exception.expect(IllegalArgumentException.class);
        exception.expectMessage("The specified file was empty");

        File emptyImage = new File("misc/empty.jpg");
        this.client.getImageIdentifier(emptyImage);
    }

    /**
     * The client must be able to generate an image identifier based on a byte array
     */
    @Test
    public void testCanGenerateAnImageIdentifierBasedOnByteArray() {
    	assertSame(
			"f9137fdccf9694912f3331e1f96ea72f",
			this.client.getImageIdentifier(readFile(new File("misc/imbo-logo.png")))
		);
        
        assertSame(
			"0f88d0234601c80a1ba634e0630fb11a",
			this.client.getImageIdentifier(readFile(new File("misc/imbo-logo.jpg")))
		);
        
        assertSame(
			"005004a9cca74dff1a7b54599abb2618",
			this.client.getImageIdentifier(readFile(new File("misc/imbo-logo.gif")))
		);
    }

    /**
     * The client must be able to correctly generate signatures based on the HTTP method, the URL
     * and a timestamp
     * 
     * @throws NoSuchMethodException 
     * @throws SecurityException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @Test
    public void testCanGenerateValidSignaturesForRemoteWriteOperations() throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    	String[] data = new String[] {
    		"PUT", "http://imbo/users/" + this.publicKey + "/images/" + this.imageIdentifier, "2012-03-14T10:04:06Z",
    		"2237c6da85b7270e443ce07e2788e0df858abba3e83059b984alcd2822c36d2b4ba",
    		
            "PUT", "http://imbo/users/" + this.publicKey + "/images/" + this.imageIdentifier, "2012-03-14T10:04:07Z",
            "6b0db609609a6dec864a50ad41bdd1077df04f8f1bf40104cde249ad913c6ec3",
            
            "POST", "http://imbo/users/" + this.publicKey + "/images/" + this.imageIdentifier + "/meta", "2012-03-14T10:04:06Z",
            "29efe947d53fa0e51d416a5a05f01420ec27c8f182c44b63186574b5d54f8a8c",
            
            "POST", "http://imbo/users/" + this.publicKey + "/images/" + this.imageIdentifier + "/meta", "2012-03-14T10:04:07Z",
            "c6cd382a60b4bd988a2d8426000af353e7a5569a42604e5a30a7a65bce77d334",
            
            "DELETE", "http://imbo/users/" + this.publicKey + "/images/" + this.imageIdentifier, "2012-03-14T10:04:06Z",
            "48d605563d761d2155939873fe4a820fbcac69c1d3e3df82c3ac92097f2ae9f2",
            
            "DELETE", "http://imbo/users/" + this.publicKey + "/images/" + this.imageIdentifier, "2012-03-14T10:04:07Z",
            "242f79f025ce7c463b8826e87c335293c2bf552bea11ea6b47c5ed5ae6657061"
    	};
    	
    	Client client;
    	Method m;
    	Object[] parameters;
    	String result;
    	
    	for (int i = 0; i < data.length; i += 4) {
    		client = new Client(this.serverUrl, this.publicKey, this.privateKey);
        	
            m = client.getClass().getDeclaredMethod("generateSignature", new Class[] {
        		java.lang.String.class,
        		java.lang.String.class,
        		java.lang.String.class
        	});
            m.setAccessible(true);
          
            parameters = new Object[3];
            parameters[0] = data[i];
            parameters[1] = data[i + 1];
            parameters[2] = data[i + 2];
            
            result = (String) m.invoke(client, parameters);
            
            assertSame(data[i + 3], result);
    	}
    }

    /**
     * The client must be able to fetch server status
     * 
     * @throws IOException 
     * @throws JSONException 
     */
    @Test
    public void testCanGetServerStatusWhenServerRespondsWithHttp200() throws IOException, JSONException {
        final ResponseInterface response = getResponseMock();
        
        context.checking(new Expectations() {{
        	oneOf(response).getBody();
        	will(returnValue("{\"date\":\"some date\",\"database\":true,\"storage\":true}"));
        }});
        
        context.checking(new Expectations() {{
        	oneOf(httpClient).get(with(any(URI.class)));
        	will(returnValue(response));
        }});

        JSONObject status = this.client.getServerStatus();
        
        assertNotNull(status);
        assertTrue(status.getBoolean("database"));
        assertTrue(status.getBoolean("storage"));
    }
    
    /**
     * The client must be able to fetch server status when the server response with HTTP 500
     * Internal Server Error
     * 
     * @throws IOException 
     * @throws JSONException 
     */
    @Test
    public void testCanGetServerStatusWhenServerRespondsWithHttp500() throws IOException, JSONException {
        final ResponseInterface response = getResponseMock();
        
        context.checking(new Expectations() {{
        	oneOf(response).getBody();
        	will(returnValue("{\"date\":\"some date\",\"database\":true,\"storage\":false}"));
        }});
        
        final ServerException exception = new ServerException("Internal server error", 500);
        exception.setResponse(response);
        
        context.checking(new Expectations() {{
        	oneOf(httpClient).get(with(any(URI.class)));
        	will(throwException(exception));
        }});

        JSONObject status = this.client.getServerStatus();
        
        assertNotNull(status);
        assertTrue(status.getBoolean("database"));
        assertFalse(status.getBoolean("storage"));
    }

    /**
     * When trying to fetch server status and the server responds with an error (other than HTTP
     * 500 Internal Server Error) the client must throw an exception
     * 
     * @throws IOException 
     */
    @Test
    public void testThrowsExceptionWhenTryingToGetServerStatusAndServerRespondsWithErrorOtherThanHttp500() throws IOException {
    	exception.expect(FileNotFoundException.class);
        exception.expectMessage("The system cannot find the file specified");
        
        context.checking(new Expectations() {{
        	oneOf(httpClient).get(with(any(URI.class)));
        	will(throwException(new ServerException("Bad Request", 400)));
        }});

        this.client.getServerStatus();
    }

    /**
     * When trying to fetch server status and the server does not respond the client must throw an
     * exception
     * 
     * @throws IOException 
     */
    @Test
    public void testThrowsExceptionWhenTryingToGetServerStatusAndServerDoesNotRespond() throws IOException {
    	exception.expect(RuntimeException.class);
        exception.expectMessage("An error occured");
    	
        context.checking(new Expectations() {{
        	oneOf(httpClient).get(with(any(URI.class)));
        	will(throwException(new RuntimeException("An error occured")));
        }});
        
        this.client.getServerStatus();
    }

    /**
     * The client must be able to parse different types of "lazy" URLs passed to the constructor
     * 
     * @throws ClassNotFoundException 
     * @throws NoSuchMethodException 
     * @throws SecurityException 
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @Test
    public void testCanParseUrlsMissingHttp() throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
    	
		Client client = new Client(this.serverUrl, this.publicKey, this.privateKey);
    	
        Method m = client.getClass().getDeclaredMethod("parseUrls",  new Class[] {
    	    Class.forName("[Ljava.lang.String;")
    	});
        m.setAccessible(true);
      
        Object[] parameters = new Object[1];
        parameters[0] = new String[] {
    		"imbo",
            "http://imbo",
            "https://imbo",
            "imbo2",
        };
        
        String[] result = (String[]) m.invoke(client, parameters);
        String[] expected = new String[] {
        	"http://imbo",
        	"https://imbo",
        	"http://imbo2"
        };
        
        assertSame(expected, result);
    }

    /**
     * The client must be able to fetch user information 
     */
    @Test
    public void testCanGetUserInfoWhenServerRespondsWithHttp200() throws IOException, JSONException {
        final ResponseInterface response = getResponseMock();
        
        context.checking(new Expectations() {{
        	oneOf(response).getBody();
        	will(returnValue("{\"publicKey\":\"espen\",\"numImages\":11,\"lastModified\":\"Sun, 07 Apr 2013 20:11:31 GMT\"}"));
        }});
        
        context.checking(new Expectations() {{
        	oneOf(httpClient).get(with(any(URI.class)));
        	will(returnValue(response));
        }});
        
        JSONObject info = this.client.getUserInfo();

        assertNotNull(info);
        assertSame(11, info.getInt("numImages"));
        assertSame("espen", info.getString("publicKey"));
        assertSame("Sun, 07 Apr 2013 20:11:31 GMT", info.getString("lastModified"));
    }
    
    protected static <T> org.hamcrest.Matcher<URI> uriMatches(String regex) {
        return UriMatches(regex);
    }

    protected ResponseInterface getResponseMock() {
        return context.mock(ResponseInterface.class, "response" + (++mockCount));
    }
    
    protected QueryInterface getQueryMock() {
    	return context.mock(QueryInterface.class, "query" + (++mockCount));
    }
    
    protected byte[] readFile(File input) {
    	byte [] fileData = new byte[(int) input.length()];
    	DataInputStream dis;
		try {
			dis = new DataInputStream(new FileInputStream(input));
			dis.readFully(fileData);
	    	dis.close();
		} catch (FileNotFoundException e) {
			// This shouldn't happen as we only use it internally
		} catch (IOException e) {
			// This shouldn't happen as we only use it internally
		}
    	
    	return fileData;
    }

}