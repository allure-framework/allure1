package ru.yandex.qatools.allure.data.io;

import ru.yandex.qatools.allure.commons.AllureFileUtils;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.inject.Inject;
import javax.xml.bind.JAXB;
import java.io.File;
import java.util.Iterator;

/**
 * eroshenkoam
 * 02/02/15
 */
public class TestSuiteReader implements Reader<TestSuiteResult> {

    private final Iterator<File> testSuiteResultFiles;

    @Inject
    public TestSuiteReader(@ResultDirectory File... resultDirectories) {
        testSuiteResultFiles = AllureFileUtils.listTestSuiteFiles(resultDirectories).iterator();
    }

    @Override
    public Iterator<TestSuiteResult> iterator() {
        return new Iterator<TestSuiteResult>() {
            @Override
            public boolean hasNext() {
                return testSuiteResultFiles.hasNext();
            }

            @Override
            public TestSuiteResult next() {
                if (!hasNext()) {
                    return null;
                }
                return JAXB.unmarshal(testSuiteResultFiles.next(), TestSuiteResult.class);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
