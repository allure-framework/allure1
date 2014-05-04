package ru.yandex.qatools.allure.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.io.File;

import static org.hamcrest.CoreMatchers.not;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 04.05.14
 */
public class DirectoryMatcher {
    public static class Contains<T> extends BaseMatcher<T> {

        private String fileName;

        public Contains(String fileName) {
            this.fileName = fileName;
        }

        @Override
        public boolean matches(Object item) {
            if (!(item instanceof File)) {
                return false;
            }

            File dir = (File) item;
            return dir.isDirectory() && !FileUtils.listFiles(
                    dir,
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
        return new Contains<>(fileName);
    }

    public static Matcher notContains(String fileName) {
        return not(contains(fileName));
    }
}
