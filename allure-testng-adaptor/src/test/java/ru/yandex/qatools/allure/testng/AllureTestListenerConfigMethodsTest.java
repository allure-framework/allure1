package ru.yandex.qatools.allure.testng;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import ru.yandex.qatools.allure.commons.AllureFileUtils;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.allure.testng.AllureTestListener.ConfigMethodType;
import ru.yandex.qatools.allure.testng.testdata.ConfigMethodsTest;
import ru.yandex.qatools.allure.utils.AllureResultsUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Test for various TestNg configuration method skip behavior
 *
 * @author Michael Braiman braimanm@gmail.com
 *         Date: 15.06.15
 */
public class AllureTestListenerConfigMethodsTest {

    private static final String ALLURE_RESULTS = "allure-results";

    private static Path resultsDir;

    private static final String SUITE_PREFIX = "SUITE";

    @BeforeClass
    public static void setUp() throws IOException {
        resultsDir = Files.createTempDirectory(ALLURE_RESULTS);
        AllureResultsUtils.setResultsDirectory(resultsDir.toFile());

        List<XmlSuite> suites = new ArrayList<>();
        for (ConfigMethodType type : ConfigMethodType.values()) {
            suites.add(createSuite(type.getTitle()));
        }

        TestNG testNG = new TestNG();
        testNG.setXmlSuites(suites);
        testNG.setUseDefaultListeners(false);
        testNG.run();
    }

    private static XmlSuite createSuite(String testName) {
        XmlSuite suite = new XmlSuite();
        suite.setName(SUITE_PREFIX + testName);
        XmlTest test = new XmlTest(suite);
        test.setName(testName);
        List<XmlClass> classes = new ArrayList<>();
        classes.add(new XmlClass(ConfigMethodsTest.class));
        test.setXmlClasses(classes);
        return suite;
    }


    @AfterClass
    public static void tearDown() throws IOException {
        AllureResultsUtils.setResultsDirectory(null);
        AllureTestUtils.deleteNotEmptyDirectory(resultsDir);
    }


    @Test
    public void validateCanceledTest() throws IOException {
        List<TestSuiteResult> results = AllureFileUtils.unmarshalSuites(resultsDir.toFile());
        for (TestSuiteResult result : results) {
            validateTestSuiteResult(result);
        }
    }


    private void validateTestSuiteResult(TestSuiteResult testSuiteResult) {
        String brokenMethod = testSuiteResult.getName().replace(SUITE_PREFIX, "config");
        for (TestCaseResult result : testSuiteResult.getTestCases()) {
            String methodName = result.getName();
            Status status = result.getStatus();
            Status expectedStatus = Status.CANCELED;
            if (brokenMethod.startsWith(methodName)) {
                expectedStatus = Status.BROKEN;
            }
            if (brokenMethod.contains("After") && methodName.equalsIgnoreCase("test")) {
                expectedStatus = Status.PASSED;
            }
            assertThat(String.format("Wrong status for test <%s>, method <%s>", brokenMethod, methodName),
                    status, equalTo(expectedStatus));
        }
    }


}
