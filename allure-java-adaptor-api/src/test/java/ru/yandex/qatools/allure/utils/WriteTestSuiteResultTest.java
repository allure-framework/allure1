package ru.yandex.qatools.allure.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.File;
import java.nio.file.Files;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ru.yandex.qatools.allure.commons.AllureFileUtils.listTestSuiteFiles;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 19.02.14
 */
public class WriteTestSuiteResultTest {

    public File resultsDirectory;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        resultsDirectory = folder.newFolder();
        AllureResultsUtils.setResultsDirectory(resultsDirectory);
    }

    @Test
    public void invalidCharacterTest() throws Exception {
        TestSuiteResult testSuiteResult = new TestSuiteResult()
                .withName("somename");

        String titleWithInvalidXmlCharacter = String.valueOf(Character.toChars(0x0));
        testSuiteResult.setTitle(titleWithInvalidXmlCharacter);

        AllureResultsUtils.writeTestSuiteResult(testSuiteResult);

        Validator validator = AllureModelUtils.getAllureSchemaValidator();

        for (File each : listTestSuiteFiles(resultsDirectory)) {
            validator.validate(new StreamSource(each));
        }
    }

    @Test
    public void shouldCloseResultFileTest() throws Exception {
        File resultFile = new File(folder.getRoot(), "suite.xml");

        AllureResultsUtils.writeTestSuiteResult(new TestSuiteResult(), resultFile);

        assertTrue(resultFile.exists());

        Files.delete(resultFile.toPath());
        assertFalse(resultFile.exists());
    }

    @After
    public void tearDown() {
        AllureResultsUtils.setResultsDirectory(null);
    }
}
