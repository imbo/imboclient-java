/**
 * Regex matcher for Hamcrest
 *
 * @author Piotr Gabryanczyk <http://piotrga.wordpress.com/2009/03/27/hamcrest-regex-matcher/>
 */
package io.imbo.client.util;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class RegexMatcher extends BaseMatcher<String> {
    private final String regex;

    public RegexMatcher(String regex) {
        this.regex = regex;
    }

    @Override
    public boolean matches(Object o) {
        return ((String) o).matches(regex);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("matches regex=");
    }

    public static RegexMatcher matches(String regex) {
        return new RegexMatcher(regex);
    }
}