package ru.yandex.qatools.allure.data.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.AllureUtils;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * eroshenkoam
 * 02/02/15
 */
public class TestSuiteReader implements Reader<TestSuiteResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestSuiteReader.class);

    private final Iterator<Path> testSuiteResultFiles;

    @Inject
    public TestSuiteReader(@ResultDirectories Path... resultDirectories) throws IOException {
        testSuiteResultFiles = AllureUtils.listTestSuiteFiles(resultDirectories).iterator();
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
            Path next = testSuiteResultFiles.next();
            try {
                return AllureUtils.unmarshalTestSuite(next);
            } catch (IOException e) {
                LOGGER.warn(String.format("Could not read <%s> file", next.toAbsolutePath().toString()), e);
                return next();
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
