package ru.yandex.qatools.allure.utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.DummyConfig;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.nio.file.Path;

import static ru.yandex.qatools.allure.AllureUtils.validateResults;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 19.02.14
 */
public class WriteTestSuiteResultTest {

    private Path resultsDirectory;

    private AllureResultsHelper helper;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        resultsDirectory = folder.newFolder().toPath();
        helper = new AllureResultsHelper(new DummyConfig(resultsDirectory));
    }

    @Test
    public void invalidCharacterTest() throws Exception {
        TestSuiteResult testSuiteResult = new TestSuiteResult()
                .withName("somename");

        String titleWithInvalidXmlCharacter = String.valueOf(Character.toChars(0x0));
        testSuiteResult.setTitle(titleWithInvalidXmlCharacter);

        helper.writeTestSuite(testSuiteResult);
        validateResults(resultsDirectory);
    }
}
