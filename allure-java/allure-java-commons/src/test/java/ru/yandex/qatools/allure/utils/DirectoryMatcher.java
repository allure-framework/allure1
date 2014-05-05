package ru.yandex.qatools.allure.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.io.File;

import static org.hamcrest.CoreMatchers.not;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 04.05.14
 */
public class DirectoryMatcher {
    public static class Contains extends TypeSafeMatcher<File> {

        private String fileName;

        public Contains(String fileName) {
            this.fileName = fileName;
        }

        @Override
        protected boolean matchesSafely(File directory) {
            return directory.isDirectory() && !FileUtils.listFiles(
                    directory,
                    new NameFileFilter(fileName),
                    TrueFileFilter.INSTANCE
            ).isEmpty();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("contains file ").appendValue(fileName);
        }
    }

    public static Matcher contains(String fileName) {
        return new Contains(fileName);
    }

    public static Matcher notContains(String fileName) {
        return not(contains(fileName));
    }
}
