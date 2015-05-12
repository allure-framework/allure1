package ru.yandex.qatools.allure.data.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.commons.AllureFileUtils;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import static ru.yandex.qatools.allure.commons.AllureFileUtils.unmarshal;

/**
 * eroshenkoam
 * 02/02/15
 */
public class TestSuiteReader implements Reader<TestSuiteResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestSuiteReader.class);

    private final Iterator<File> testSuiteResultFiles;

    @Inject
    public TestSuiteReader(@ResultDirectories File... resultDirectories) {
        testSuiteResultFiles = AllureFileUtils.listTestSuiteFiles(resultDirectories).iterator();
    }

    @Override
    public Iterator<TestSuiteResult> iterator() {
        return new TestSuiteResultIterator();
    }

    private class TestSuiteResultIterator implements Iterator<TestSuiteResult> {
        @Override
        public boolean hasNext() {
            return testSuiteResultFiles.hasNext();
        }

        @Override
        public TestSuiteResult next() {
            if (!hasNext()) {
                return null;
            }
            File next = testSuiteResultFiles.next();
            try {
                return unmarshal(next);
            } catch (IOException e) {
                LOGGER.warn(String.format("Could not read <%s> file", next.getAbsoluteFile()), e);
                return next();
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
