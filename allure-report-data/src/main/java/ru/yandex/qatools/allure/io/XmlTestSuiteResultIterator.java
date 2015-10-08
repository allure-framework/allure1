package ru.yandex.qatools.allure.io;

import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.io.IOException;
import java.nio.file.Path;

import static ru.yandex.qatools.allure.AllureConstants.TEST_SUITE_XML_FILE_GLOB;
import static ru.yandex.qatools.allure.AllureUtils.unmarshalTestSuite;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 08.10.15
 */
public class XmlTestSuiteResultIterator extends AbstractResultIterator<TestSuiteResult> {

    /**
     * Creates an instance of iterator.
     */
    public XmlTestSuiteResultIterator(Path... resultDirectories) throws IOException {
        super(resultDirectories);
    }

    /**
     * Read XML test suite result.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    protected TestSuiteResult readResult(Path path) throws IOException {
        return unmarshalTestSuite(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFilesGlob() {
        return TEST_SUITE_XML_FILE_GLOB;
    }
}
