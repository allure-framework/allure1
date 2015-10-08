package ru.yandex.qatools.allure;

import org.junit.Test;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.allure.testdata.ConfigMethodsTest;
import ru.yandex.qatools.allure.testng.AllureTestListener.ConfigMethodType;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static ru.yandex.qatools.allure.AllureUtils.listTestSuiteXmlFiles;

/**
 * Test for various TestNg configuration method skip behavior
 *
 * @author Michael Braiman braimanm@gmail.com
 *         Date: 15.06.15
 */
public class AllureTestListenerConfigMethodsTest extends BasicListenerTest {

    private static final String SUITE_PREFIX = "SUITE";

    @Override
    public void configure(TestNG testNG) {
        List<XmlSuite> suites = new ArrayList<>();
        for (ConfigMethodType type : ConfigMethodType.values()) {
            suites.add(createSuite(type.getTitle()));
        }
        testNG.setXmlSuites(suites);
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


    @Test
    public void validateCanceledTest() throws IOException {
        for (Path file : listTestSuiteXmlFiles(resultsDirectory)) {
            TestSuiteResult result = JAXB.unmarshal(file.toFile(), TestSuiteResult.class);
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
