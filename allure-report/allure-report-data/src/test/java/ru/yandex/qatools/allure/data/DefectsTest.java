package ru.yandex.qatools.allure.data;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;
import ru.yandex.qatools.allure.config.AllureResultsConfig;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.List;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.selectFirst;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/10/13
 */
@Ignore
public class DefectsTest {

    public static final String TEST_DEFECTS = "Test defects";
    public static final String PRODUCT_DEFECTS = "Product defects";
    private static AllureResultsConfig resultsConfig = AllureResultsConfig.newInstance();

    @ClassRule
    public static AllureReportGenerationRule allureRule =
            new AllureReportGenerationRule(resultsConfig.getResultsDirectory());

    private AllureDefects allureDefectsData;

    private List<TestSuiteResult> testSuiteResults;

    @Before
    public void initVariables() throws Exception {
        this.allureDefectsData = allureRule.getDefectsData();
        this.testSuiteResults = allureRule.getTestSuiteResults();
    }

    @Test
    public void shouldContainProductDefects() {
        AllureDefect defect = selectFirst(allureDefectsData.defectsList,
                having(on(AllureDefect.class).getTitle(), equalTo(PRODUCT_DEFECTS)));

        assertThat(defect, notNullValue());
    }

    @Test
    public void shouldContainTestDefects() {
        AllureDefect defect = selectFirst(allureDefectsData.defectsList,
                having(on(AllureDefect.class).getTitle(), equalTo(TEST_DEFECTS)));

        assertThat(defect, notNullValue());
    }

    @Test
    public void productDefectsSizeShouldEqualToNumberOfFailedTestCases() {
        AllureDefect defect = selectFirst(allureDefectsData.defectsList,
                having(on(AllureDefect.class).getTitle(), equalTo(PRODUCT_DEFECTS)));

        int resultsTestCasesCount = 0;

        for (TestSuiteResult suite : testSuiteResults) {
            resultsTestCasesCount += select(suite.getTestCases(),
                    having(on(TestCaseResult.class).getStatus(), equalTo(Status.FAILED))).size();
        }

        assertThat(defect.defects, hasSize(equalTo(resultsTestCasesCount)));
    }

    @Test
    public void testDefectsSizeShouldEqualToNumberOfBrokenTestCases() {
        AllureDefect defect = selectFirst(allureDefectsData.defectsList,
                having(on(AllureDefect.class).getTitle(), equalTo(TEST_DEFECTS)));

        int resultsTestCasesCount = 0;

        for (TestSuiteResult suite : testSuiteResults) {
            resultsTestCasesCount += select(suite.getTestCases(),
                    having(on(TestCaseResult.class).getStatus(), equalTo(Status.BROKEN))).size();
        }

        assertThat(defect.defects, hasSize(equalTo(resultsTestCasesCount)));
    }

}
