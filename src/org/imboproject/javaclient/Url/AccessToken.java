package org.imboproject.javaclient.Url;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class AccessToken implements AccessTokenInterface {

	/**
     * {@inheritDoc}
     */
	@Override
	public String generateToken(String url, String key) {

	    Charset charset = Charset.forName("UTF-8");
	    String algoName = "HmacSHA256";
	    Mac algorithm = null;
		try {
			algorithm = Mac.getInstance(algoName);
		} catch (NoSuchAlgorithmException e) {
			// This should hopefully never happen
			return "hmac-sha-256-algorithm-not-found";
		}

		byte[] byteKey = charset.encode(key).array();
	    SecretKeySpec secretKey = new javax.crypto.spec.SecretKeySpec(byteKey, algoName);
	    try {
	    	algorithm.init(secretKey);
	    } catch (InvalidKeyException e) {
	        // .. And this shouldn't really ever happen, either
	    	return "invalid-key-for-access-token-generation";
	    }
        
	    final byte[] macData = algorithm.doFinal(charset.encode(url).array());
	    
	    String result = "";
        for (final byte element : macData) {
           result += Integer.toString((element & 0xff) + 0x100, 16).substring(1);
        }
        
		return result;
	}
	
}