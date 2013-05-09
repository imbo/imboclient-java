/**
 * This file is part of the imboclient-java package
 *
 * (c) Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 * For the full copyright and license information, please view the LICENSE file that was
 * distributed with this source code.
 */
package org.imboproject.javaclient.util;

import java.net.URI;

import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * @author Espen Hovlandsdal <espen@hovlandsdal.com>
 *
 */
public class UriMatches extends TypeSafeMatcher<URI> {

    private String regex;

    public void setRegex(String regex) {
        this.regex = regex;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("does not match regex");
    }

    @Override
    protected boolean matchesSafely(URI url) {
        return url.toString().matches(regex);
    }

    @Factory
    public static <T> Matcher<URI> UriMatches(String regex) {
        UriMatches matcher = new UriMatches();
        matcher.setRegex(regex);

        return matcher;
    }

}