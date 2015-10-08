package ru.yandex.qatools.allure.io;

import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.yandex.qatools.allure.AllureConstants.TEST_SUITE_JSON_FILE_GLOB;
import static ru.yandex.qatools.allure.utils.AllureReportUtils.createMapperWithJaxbAnnotationInspector;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 08.10.15
 */
public class JsonTestSuiteResultIterator extends AbstractResultIterator<TestSuiteResult> {

    /**
     * Creates an instance of iterator.
     */
    public JsonTestSuiteResultIterator(Path... resultDirectories) throws IOException {
        super(resultDirectories);
    }

    /**
     * Read JSON test suite result.
     * <p/>
     * {@inheritDoc}
     */
    @Override
    protected TestSuiteResult readResult(Path path) throws IOException {
        try (InputStream is = Files.newInputStream(path)) {
            return createMapperWithJaxbAnnotationInspector()
                    .readValue(is, TestSuiteResult.class);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFilesGlob() {
        return TEST_SUITE_JSON_FILE_GLOB;
    }
}
