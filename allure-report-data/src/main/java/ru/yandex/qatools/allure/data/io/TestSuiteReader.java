package ru.yandex.qatools.allure.data.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.commons.AllureFileUtils;
import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.allure.data.utils.BadXmlCharacterFilterReader;

import javax.inject.Inject;
import javax.xml.bind.JAXB;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

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
                File next = testSuiteResultFiles.next();
                try {
                    java.io.Reader reader = new InputStreamReader(new FileInputStream(next), StandardCharsets.UTF_8);
                    return JAXB.unmarshal(new BadXmlCharacterFilterReader(reader), TestSuiteResult.class);
                } catch (FileNotFoundException e) {
                    LOGGER.warn("Could not read testsuite.xml file", e);
                    return next();
                }
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
