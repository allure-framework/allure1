package ru.yandex.qatools.allure;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.not;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 30.09.15
 */
public class Matchers {

    public static class PathExists extends TypeSafeMatcher<Path> {

        @Override
        protected boolean matchesSafely(Path item) {
            return Files.exists(item);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("file exists");
        }
    }

    public static Matcher exists() {
        return new PathExists();
    }

    public static Matcher notExists() {
        return not(exists());
    }
}
