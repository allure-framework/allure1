package ru.yandex.qatools.allure.data;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import ru.yandex.qatools.allure.config.AllureResultsConfig;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.List;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/10/13
 */
public class XUnitStatisticsTest {

    private static AllureResultsConfig resultsConfig = AllureResultsConfig.newInstance();

    @ClassRule
    public static AllureReportGenerationRule allureRule =
            new AllureReportGenerationRule(resultsConfig.getResultsDirectory());

    private AllureXUnit allureXUnitData;

    private List<TestSuiteResult> testSuiteResults;

    @Before
    public void initVariables() throws Exception {
        this.allureXUnitData = allureRule.getXUnitData();
        this.testSuiteResults = allureRule.getTestSuiteResults();
    }

    @Test
    public void xUnitStartTimeShouldBeMinimumStartTimeOfAllTestSuites() {
        assertThat(allureXUnitData.getTime().getStart(), equalTo(minFrom(testSuiteResults).getStart()));
    }

    @Test
    public void xUnitStopTimeShouldBeMaximumStopTimeOfAllTestSuites() {
        assertThat(allureXUnitData.getTime().getStop(), equalTo(maxFrom(testSuiteResults).getStop()));
    }

    @Test
    public void xUnitDurationTimeShouldDifferenceBetweenStopTimeAndStartTime() {
        assertThat(allureXUnitData.getTime().getDuration(),
                equalTo(allureXUnitData.getTime().getStop() - allureXUnitData.getTime().getStart()));
    }

    @Test
    public void xUnitTestSuitesCountMustBeEqualsResultsTestSuitesCount() {
        assertThat(allureXUnitData.getTestSuites().size(), equalTo(testSuiteResults.size()));
    }

    @Test
    public void xUnitTestCasesCountMustBeEqualsResultsTestCasesCount() {
        int xUnitTestCasesCount = sum(allureXUnitData.getTestSuites(), on(AllureTestSuite.class).getTestCases().size());
        int resultsTestCasesCount = sum(testSuiteResults, on(TestSuiteResult.class).getTestCases().size());
        assertThat(xUnitTestCasesCount, equalTo(resultsTestCasesCount));
    }
}
