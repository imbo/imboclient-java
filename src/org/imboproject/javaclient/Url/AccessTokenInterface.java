package org.imboproject.javaclient.Url;

public interface AccessTokenInterface {

	/**
	 * Generate an access token for a given URL using a key
	 * 
	 * @param url The URL to generate the token for
	 * @param key The key to use when generating the token
	 * @return Returns an access token for a URL. Given the same URL and key combo this method returns the same token every time.
	 */
	public String generateToken(String url, String key);
	
}
