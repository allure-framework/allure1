package ru.yandex.qatools.allure;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;
import org.openqa.selenium.WebDriver;

import java.net.URI;

public class UrlFragmentMatcher extends TypeSafeMatcher<WebDriver> {

    private String expectedFragment;

    public UrlFragmentMatcher(String fragment) {
        expectedFragment = fragment;
    }

    private String getUrlFragment(WebDriver driver) {
        return URI.create(driver.getCurrentUrl()).getFragment();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Fragment should be equal \"").appendText(expectedFragment).appendText("\"");
    }

    @Override
    protected void describeMismatchSafely(WebDriver driver, Description mismatchDescription) {
        mismatchDescription.appendText("was \"").appendText(getUrlFragment(driver)).appendText("\"");
    }

    @Override
    protected boolean matchesSafely(WebDriver driver) {
        return expectedFragment.equals(getUrlFragment(driver));
    }

    public static UrlFragmentMatcher hasFragment(String fragment) {
        return new UrlFragmentMatcher(fragment);
    }
}
