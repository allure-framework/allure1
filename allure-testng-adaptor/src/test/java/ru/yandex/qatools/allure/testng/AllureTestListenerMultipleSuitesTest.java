package ru.yandex.qatools.allure.testng;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testng.TestNG;

import com.beust.jcommander.internal.Lists;

import ru.yandex.qatools.allure.commons.AllureFileUtils;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.allure.utils.AllureResultsUtils;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static ru.yandex.qatools.allure.commons.AllureFileUtils.listTestSuiteFiles;

/**
 * @author Michael Braiman braimanm@gmail.com
 */
public class AllureTestListenerMultipleSuitesTest {

    private static final String SUITE1 = "/suite1.xml";
    private static final String SUITE2 = "/suite2.xml";
    private static final String ALLURE_RESULTS = "allure-results";

    private static Path resultsDir;

    @Before
    public void setUp() throws IOException {
        resultsDir = Files.createTempDirectory(ALLURE_RESULTS);
        AllureResultsUtils.setResultsDirectory(resultsDir.toFile());
        List<String> suites = Lists.newArrayList();
        suites.add(getClass().getResource(SUITE1).getFile());
        suites.add(getClass().getResource(SUITE2).getFile());
        TestNG testNG = new TestNG();
        testNG.setTestSuites(suites);
        testNG.setSuiteThreadPoolSize(2);
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
        assertThat(listTestSuiteFiles(resultsDir.toFile()).size(), equalTo(2));
    }

    @Test
    public void validateSuiteFilesTest() throws Exception {
        Validator validator = AllureModelUtils.getAllureSchemaValidator();

        for (File each : listTestSuiteFiles(resultsDir.toFile())) {
            validator.validate(new StreamSource(each));
        }
    }
    
    @Test
    public void validatePendingTest() throws IOException {
        TestSuiteResult testSuite = AllureFileUtils.unmarshalSuites(resultsDir.toFile()).get(0);
        TestCaseResult testResult = testSuite.getTestCases().get(0);

        assertThat(testResult.getStatus(), equalTo(Status.PENDING));  
        assertThat(testResult.getDescription().getValue(), equalTo("This is pending test"));
    }
    
}
