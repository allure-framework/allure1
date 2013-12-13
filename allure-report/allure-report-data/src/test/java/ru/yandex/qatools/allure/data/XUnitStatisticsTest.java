package ru.yandex.qatools.allure.data;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import ru.yandex.qatools.allure.model.AllureModelProperties;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.List;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by eroshenkoam on 12/10/13.
 */
public class XUnitStatisticsTest {

    private static AllureModelProperties allureModelProperties = new AllureModelProperties();

    @ClassRule
    public static AllureReportGenerationRule allureRule =
            new AllureReportGenerationRule(allureModelProperties.getResultsPath());

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
