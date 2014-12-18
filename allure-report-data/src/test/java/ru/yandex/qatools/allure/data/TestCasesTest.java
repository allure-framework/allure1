package ru.yandex.qatools.allure.data;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import ru.yandex.qatools.allure.config.AllureConfig;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.flatten;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 09.12.14
 */
public class TestCasesTest {

    private static final AllureConfig CONFIG = AllureConfig.newInstance();

    @ClassRule
    public static AllureReportGenerationRule allureRule =
            new AllureReportGenerationRule(CONFIG.getResultsDirectory());

    private List<AllureTestCase> allureTestCases;

    private List<TestSuiteResult> testSuiteResults;

    @Before
    public void initVariables() throws Exception {
        this.allureTestCases = allureRule.getTestCasesData();
        this.testSuiteResults = allureRule.getTestSuiteResults();
    }

    @Test
    public void testCasesCountTest() throws Exception {
        assertThat(flatten(extract(testSuiteResults, on(TestSuiteResult.class).getTestCases())), hasSize(allureTestCases.size()));
    }

    @Test
    public void testCasesShouldContainsAllActualTitles() throws Exception {
        List<TestCaseResult> testCases = flatten(extract(testSuiteResults, on(TestSuiteResult.class).getTestCases()));
        List<String> expected = extract(select(testCases, having(on(TestCaseResult.class).getTitle(), notNullValue())), on(TestCaseResult.class).getTitle());
        List<String> actual = extract(allureTestCases, on(AllureTestCase.class).getTitle());

        assertThat(actual, Matchers.hasItems(expected.toArray(new String[expected.size()])));
    }

    @Test
    public void dataShouldNotBeEmpty() {
        assertThat(testSuiteResults, is(not(empty())));
        List<TestCaseResult> testCases = flatten(extract(testSuiteResults, on(TestSuiteResult.class).getTestCases()));
        assertThat(testCases, is(not(empty())));
    }
}
