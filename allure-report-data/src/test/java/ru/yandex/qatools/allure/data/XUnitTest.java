package ru.yandex.qatools.allure.data;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import ru.yandex.qatools.allure.config.AllureResultsConfig;
import ru.yandex.qatools.allure.model.*;

import java.util.List;
import java.util.Map;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/10/13
 */
public class XUnitTest {

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
    public void xUnitSuiteDescriptionTest() throws Exception {
        int size = testSuiteResults.size();
        for (int i = 0; i < size; i++) {
            Description xUnitDescription = allureXUnitData.getTestSuites().get(i).getDescription();
            Description resultsDescription = testSuiteResults.get(i).getDescription();
            assertThat(xUnitDescription == null, equalTo(resultsDescription == null));
            if (xUnitDescription != null && resultsDescription != null) {
                assertThat(
                        xUnitDescription.getValue(),
                        equalTo(resultsDescription.getValue())
                );
            }
        }
    }

    @Test
    public void xUnitSeverityNotNullTest() {
        for (AllureTestSuite testSuite : allureXUnitData.getTestSuites()) {
            for (AllureTestCaseInfo testCase : testSuite.getTestCases()) {
                assertNotNull(testCase.getSeverity());
            }
        }
    }

    @Test
    public void xUnitSeverityTest() {
        List<TestCaseResult> testCases = flatten(extract(testSuiteResults, on(TestSuiteResult.class).getTestCases()));
        List<Label> labels = flatten(extract(testCases, on(TestCaseResult.class).getLabels()));
        List<Label> severityLabels = filter(
                having(on(Label.class).getName(), equalTo(LabelName.SEVERITY.value())),
                labels
        );
        Map<String, Integer> counts = count(severityLabels, on(Label.class).getValue());

        checkSeverity(counts, SeverityLevel.BLOCKER);
        checkSeverity(counts, SeverityLevel.MINOR);
        checkSeverity(counts, SeverityLevel.CRITICAL);
        checkSeverity(counts, SeverityLevel.TRIVIAL);
    }

    public void checkSeverity( Map<String, Integer> counts, SeverityLevel level) {
        int modify = 0;
        for (AllureTestSuite testSuite : allureXUnitData.getTestSuites()) {
            for (AllureTestCaseInfo testCase : testSuite.getTestCases()) {
                if (testCase.getSeverity().equals(level)) {
                    modify++;
                }
            }
        }

        int origin = counts.get(level.value()) == null ? 0 : counts.get(level.value());
        assertEquals(origin, modify);
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