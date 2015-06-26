package ru.yandex.qatools.allure.testng;

import org.junit.*;
import org.testng.TestNG;

import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.testng.testdata.TestDataClass;
import ru.yandex.qatools.allure.utils.AllureResultsUtils;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;

import java.io.*;
import java.nio.file.*;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static ru.yandex.qatools.allure.commons.AllureFileUtils.listTestSuiteFiles;

/**
 * @author Kirill Kozlov kozlov.k.e@gmail.com
 *         Date: 04.02.14
 */
public class AllureTestListenerXmlValidationTest {

    private static final String DEFAULT_SUITE_NAME = "suite";
    private static final String ALLURE_RESULTS = "allure-results";

    private static Path resultsDir;

    @Before
    public void setUp() throws IOException {
        resultsDir = Files.createTempDirectory(ALLURE_RESULTS);
        AllureResultsUtils.setResultsDirectory(resultsDir.toFile());

        TestNG testNG = new TestNG();
        testNG.setDefaultSuiteName(DEFAULT_SUITE_NAME);
        testNG.setTestClasses(new Class[]{TestDataClass.class});
        testNG.setUseDefaultListeners(false);

        testNG.run();
    }

    @After
    public void tearDown() throws IOException {
        AllureResultsUtils.setResultsDirectory(null);
        AllureTestUtils.deleteNotEmptyDirectory(resultsDir);
    }

    @Test
    public void suiteFilesCountTest() throws Exception {
        assertThat(listTestSuiteFiles(resultsDir.toFile()).size(), is(1));
    }

    @Test
    public void validateSuiteFilesTest() throws Exception {
        Validator validator = AllureModelUtils.getAllureSchemaValidator();

        for (File each : listTestSuiteFiles(resultsDir.toFile())) {
            validator.validate(new StreamSource(each));
        }
    }
}