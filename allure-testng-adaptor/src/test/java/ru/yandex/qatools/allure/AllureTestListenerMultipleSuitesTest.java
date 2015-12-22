package ru.yandex.qatools.allure;

import com.beust.jcommander.internal.Lists;
import org.junit.Test;
import org.testng.TestNG;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static ru.yandex.qatools.allure.AllureUtils.listTestSuiteXmlFiles;
import static ru.yandex.qatools.allure.AllureUtils.validateResults;

/**
 * @author Michael Braiman braimanm@gmail.com
 */
public class AllureTestListenerMultipleSuitesTest extends BasicListenerTest {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void configure(TestNG testNG) throws URISyntaxException {
        List<String> suites = Lists.newArrayList();
        suites.add(getResourcePath(SUITE1));
        suites.add(getResourcePath(SUITE2));
        testNG.setTestSuites(suites);
        testNG.setSuiteThreadPoolSize(2);
    }

    @Test
    public void suiteFilesCountTest() throws Exception {
        assertThat(listTestSuiteXmlFiles(resultsDirectory).size(), equalTo(2));
    }

    @Test
    public void validateSuiteFilesTest() throws Exception {
        validateResults(resultsDirectory);
    }

    @Test
    public void validatePendingTest() throws IOException {
        TestSuiteResult testSuite = JAXB.unmarshal(
                AllureUtils.listTestSuiteXmlFiles(resultsDirectory).iterator().next().toFile(),
                TestSuiteResult.class
        );

        TestCaseResult testResult = testSuite.getTestCases().get(0);

        assertThat(testResult.getStatus(), equalTo(Status.PENDING));
        assertThat(testResult.getDescription().getValue(), equalTo("This is pending test"));
    }

}
