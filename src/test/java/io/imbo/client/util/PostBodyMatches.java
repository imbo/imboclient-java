/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package io.imbo.client.util;

import java.util.Scanner;

import org.apache.http.client.methods.HttpPost;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 */
public class PostBodyMatches extends TypeSafeMatcher<HttpPost> {

    private String body;

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("does not match expected body");
    }

    @Override
    protected boolean matchesSafely(HttpPost post) {
		String content = "";
    	try {
			Scanner s = new Scanner(post.getEntity().getContent());
			s.useDelimiter("\\A");
			
			content = s.hasNext() ? s.next() : "";
			s.close();
		} catch (Exception e) {
			
		}
		
		return content.equals(this.body);
    }

    @Factory
    public static <T> Matcher<HttpPost> PostBodyMatches(String body) {
        PostBodyMatches matcher = new PostBodyMatches();
        matcher.setBody(body);

        return matcher;
    }

}