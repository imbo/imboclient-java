/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package org.imboproject.javaclient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import junit.framework.TestCase;

import org.imboproject.javaclient.Http.ResponseInterface;
import org.imboproject.javaclient.Images.Image;
import org.imboproject.javaclient.Images.Query;
import org.imboproject.javaclient.Url.ImageUrl;
import org.imboproject.javaclient.util.OrderedRunner;
import org.imboproject.javaclient.util.OrderedRunner.Order;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

/**
 * Integration test
 *
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 */
@RunWith(OrderedRunner.class)
public class IntegrationTest extends TestCase {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    private Client client;
    private static String host = "";
	private static String publicKey = "";
	private static String privateKey = "";
	private static File logo;
	private static String logoIdentifier = "f9137fdccf9694912f3331e1f96ea72f";
	private static String now = System.currentTimeMillis() + "";
	private static String resizedIdentifier = "";

    @BeforeClass
    public static void runBeforeClass() {
    	JSONObject config = null;
    	
    	try {
    		File configFile = new File("misc/config.json");
    		String json = new Scanner(configFile, "UTF-8" ).useDelimiter("\\A").next();
    		
    		config     = new JSONObject(json);
    		host       = config.optString("host", "");
    		publicKey  = config.optString("publicKey", "");
    		privateKey = config.optString("privateKey", "");
		} catch (JSONException e) {
			System.out.println("Invalid config file - " + e.getMessage());
		} catch (FileNotFoundException e) {
			
		}
		
		boolean configValid = true;
		if (host.isEmpty() || publicKey.isEmpty() || privateKey.isEmpty()) {
			System.out.println("Host, publicKey or privateKey is empty");
			configValid = false;
		}
    	
    	org.junit.Assume.assumeTrue(configValid);
    }
    
    @Before
    public void setUp() {
        client = new Client(host, publicKey, privateKey);
        logo = new File("misc/imbo-logo.png");
    }

    @After
    public void tearDown() {
        client = null;
    }
    
    @Test
    @Order(order=1)
    public void testDeleteImageIfImageExistsWithoutErrors() throws IOException {
    	boolean exists = false;
    	try {
			exists = client.imageExists(logo);
		} catch (IllegalArgumentException e) {
			fail("IllegalArgumentException: " + e.getMessage());
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
		}
		
		if (exists) {
			String identifier = client.getImageIdentifier(logo);
			
			try {
				client.deleteImage(identifier);
			} catch (IOException e) {
				fail("IOException " + e.getMessage());
			}
		}
    }
    
    @Test
    @Order(order=2)
    public void testAddImage() {
    	deleteLogoFromServer();
    	
    	try {
			ResponseInterface res = client.addImage(logo);
			assertEquals(logoIdentifier, res.getImageIdentifier());
		} catch (ServerException e) {
			fail("ServerException: " + e.getMessage());
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
		}
    }
    
    @Test
    @Order(order=3)
    public void testAddMetadataForImage() throws JSONException {
    	JSONObject metadata = new JSONObject();
    	metadata.put("foo", "bar");
    	metadata.put("time", now);
    	
    	try {
			ResponseInterface res = client.replaceMetadata(logoIdentifier, metadata);
			assertTrue(res.isSuccess());
		} catch(ServerException e) {
			fail("ServerException: " + e.getMessage());
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
		}
    }
    
    @Test
    @Order(order=4)
    public void testGetMetadataForImage() {
    	try {
			JSONObject metadata = client.getMetadata(logoIdentifier);
			assertEquals(now, metadata.getString("time"));
			assertEquals("bar", metadata.getString("foo"));
		} catch (JSONException e) {
			fail("JSONException: " + e.getMessage());
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
		}
    }
    
    @Test
    @Order(order=5)
    public void testEditMetadataForImage() throws JSONException {
    	JSONObject metadata = new JSONObject();
    	metadata.put("edited", true);
    	
    	try {
			ResponseInterface res = client.editMetadata(logoIdentifier, metadata);
			assertTrue(res.isSuccess());
		} catch(ServerException e) {
			fail("ServerException: " + e.getMessage());
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
		}
    }
    
    @Test
    @Order(order=6)
    public void testGetEditedMetadataForImage() {
    	try {
			JSONObject metadata = client.getMetadata(logoIdentifier);
			assertEquals(now, metadata.getString("time"));
			assertEquals("bar", metadata.getString("foo"));
			assertTrue(metadata.getBoolean("edited"));
		} catch (JSONException e) {
			fail("JSONException: " + e.getMessage());
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
		}
    }
    
    @Test
    @Order(order=7)
    public void testGetNumberOfImages() {
    	try {
			int numImages = client.getNumberOfImages();
			assertTrue(numImages > 0);
		} catch (JSONException e) {
			fail("JSONException: " + e.getMessage());
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
		}
    }
    
    @Test
    @Order(order=8)
    public void testGetImages() {
    	try {
			Image[] images = client.getImages();
			assertTrue(images.length > 0);
		} catch (JSONException e) {
			fail("JSONException: " + e.getMessage());
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
		}
    }
    
    @Test
    @Order(order=9)
    public void testGetImagesWithQuery() throws JSONException {
    	JSONObject metadata = new JSONObject();
    	metadata.put("time", now);
    	
    	Query query = new Query();
    	query.limit(1);
    	query.metadataQuery(metadata);
    	
    	try {
			Image[] images = client.getImages(query);
			assertEquals(1, images.length);
			assertEquals(logoIdentifier, images[0].getIdentifier());
			assertEquals(256, images[0].getHeight());
			assertEquals(256, images[0].getWidth());
			assertEquals("image/png", images[0].getMimeType());
			assertEquals(2078, images[0].getSize());
		} catch (JSONException e) {
			fail("JSONException: " + e.getMessage());
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
		}
    }
    
    @Test
    @Order(order=10)
    public void testDeleteMetadataForImage() {
    	try {
			ResponseInterface res = client.deleteMetadata(logoIdentifier);
			assertTrue(res.isSuccess());
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
		}
    }
    
    @Test
    @Order(order=11)
    public void testGetDeletedMetadataForImage() {
    	try {
			JSONObject metadata = client.getMetadata(logoIdentifier);
			assertEquals(0, metadata.length());
		} catch (JSONException e) {
			fail("JSONException: " + e.getMessage());
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
		}
    }
    
    @Test
    @Order(order=12)
    public void testAddTransformedImageFromUrl() {
    	ImageUrl url = (ImageUrl) client.getImageUrl(logoIdentifier).resize(128, 128);
    	assertFalse(url == null);
    	try {
			ResponseInterface res = client.addImageFromUrl(url);
			assertTrue(res.isSuccess());
			
			resizedIdentifier = res.getImageIdentifier();
			assertFalse(resizedIdentifier == null || resizedIdentifier.isEmpty());
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
			e.printStackTrace();
		}
    }
    
    @Test
    @Order(order=13)
    public void testGetImageProperties() {
    	try {
			Image image = client.getImageProperties(resizedIdentifier);
			assertEquals(resizedIdentifier, image.getIdentifier());
			assertEquals(128, image.getHeight());
			assertEquals(128, image.getWidth());
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
		}
		
		try {
			client.deleteImage(resizedIdentifier);
		} catch (IOException e) {}
    }
    
    @Test
    @Order(order=14)
    public void testGetUserInfo() {
    	JSONObject info;
		try {
			info = client.getUserInfo();
			assertEquals(publicKey, info.getString("publicKey"));
			assertTrue(info.getInt("numImages") > 0);
		} catch (JSONException e) {
			fail("JSONException: " + e.getMessage());
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
		}
    }
    
    @Test
    @Order(order=15)
    public void testServerStatus() {
    	JSONObject info;
		try {
			info = client.getServerStatus();
			assertTrue(info.getBoolean("database"));
			assertTrue(info.getBoolean("storage"));
		} catch (JSONException e) {
			fail("JSONException: " + e.getMessage());
		} catch (IOException e) {
			fail("IOException: " + e.getMessage());
		}
		
		cleanup();
    }
    
    protected void deleteLogoFromServer() {
    	try {
			client.deleteImage(logoIdentifier);
		} catch (IOException e) {}
    }
    
    protected void cleanup() {
    	try {
			client.deleteImage(logoIdentifier);
			client.deleteImage(resizedIdentifier);
		} catch (Exception e) {}
    }

}