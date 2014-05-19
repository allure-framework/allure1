package ru.yandex.qatools.allure.data;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import ru.yandex.qatools.allure.config.AllureConfig;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.List;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/10/13
 */
public class DefectsTest {

    private static final String TEST_DEFECTS = "Test defects";

    private static final String PRODUCT_DEFECTS = "Product defects";

    private static final AllureConfig CONFIG = AllureConfig.newInstance();

    @ClassRule
    public static AllureReportGenerationRule allureRule =
            new AllureReportGenerationRule(CONFIG.getResultsDirectory());

    private AllureDefects allureDefectsData;

    private List<TestSuiteResult> testSuiteResults;

    @Before
    public void initVariables() throws Exception {
        allureDefectsData = allureRule.getDefectsData();
        testSuiteResults = allureRule.getTestSuiteResults();
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

        int testCasesInDefectsCount = flatten(extract(defect.defects, on(DefectItem.class).getTestCases())).size();

        assertThat(testCasesInDefectsCount, equalTo(resultsTestCasesCount));
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

        int testCasesInDefectsCount = flatten(extract(defect.defects, on(DefectItem.class).getTestCases())).size();

        assertThat(testCasesInDefectsCount, equalTo(resultsTestCasesCount));
    }

}
