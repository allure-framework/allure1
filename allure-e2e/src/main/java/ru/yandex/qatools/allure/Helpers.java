package ru.yandex.qatools.allure;

import org.hamcrest.core.CombinableMatcher;
import org.openqa.selenium.internal.WrapsElement;
import ru.yandex.qatools.htmlelements.matchers.WrapsElementMatchers;

import static org.hamcrest.core.CombinableMatcher.both;

public class Helpers {
    public static CombinableMatcher<WrapsElement> existsAndVisible() {
        return both(WrapsElementMatchers.exists()).and(WrapsElementMatchers.isDisplayed());
    }
}
