package ru.yandex.qatools.allure.events;

import org.junit.Test;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.not;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 05.05.14
 */
public class TestCasesFilterStatusEventTest {

    @Test
    public void doesNotRemoveAnyInitially() throws Exception {
        TestSuiteResult testSuiteResult = new TestSuiteResult();
        testSuiteResult.getTestCases().add(new TestCaseResult().withStatus(Status.PASSED));
        testSuiteResult.getTestCases().add(new TestCaseResult().withStatus(Status.CANCELED));
        testSuiteResult.getTestCases().add(new TestCaseResult().withStatus(Status.BROKEN));
        testSuiteResult.getTestCases().add(new TestCaseResult().withStatus(Status.FAILED));
        testSuiteResult.getTestCases().add(new TestCaseResult().withStatus(Status.PENDING));

        new TestCasesFilterStatusEvent(new ArrayList<Status>()).process(testSuiteResult);
        assertThat(testSuiteResult.getTestCases(), hasSize(5));
    }


    @Test
    public void worksWithEmptySuite() throws Exception {
        TestSuiteResult result = new TestSuiteResult();

        new TestCasesFilterStatusEvent(Arrays.asList(Status.CANCELED, Status.PASSED)).process(result);
        assertThat(result.getTestCases(), hasSize(0));
    }


    @Test
    public void removesTestCaseWithStatus() throws Exception {
        TestSuiteResult result = new TestSuiteResult();
        result.getTestCases().add(new TestCaseResult().withStatus(Status.PASSED));

        new TestCasesFilterStatusEvent(Arrays.asList(Status.PASSED)).process(result);
        assertThat(result.getTestCases(), hasSize(0));
    }

    @Test
    public void removesAllTestCasesWithStatus() throws Exception {
        TestSuiteResult result = new TestSuiteResult();
        result.getTestCases().add(new TestCaseResult().withStatus(Status.PASSED));
        result.getTestCases().add(new TestCaseResult().withStatus(Status.PASSED));
        result.getTestCases().add(new TestCaseResult().withStatus(Status.PASSED));

        new TestCasesFilterStatusEvent(Arrays.asList(Status.PASSED)).process(result);
        assertThat(result.getTestCases(), hasSize(0));
    }

    @Test
    public void removesOnlyTestCasesWithStatus() throws Exception {
        TestSuiteResult result = new TestSuiteResult();
        TestCaseResult case1 = new TestCaseResult().withStatus(Status.CANCELED);
        result.getTestCases().add(new TestCaseResult().withStatus(Status.PASSED));
        result.getTestCases().add(new TestCaseResult().withStatus(Status.PASSED));
        result.getTestCases().add(case1);

        new TestCasesFilterStatusEvent(Arrays.asList(Status.PASSED)).process(result);
        assertThat(result.getTestCases(), hasSize(1));
        assertThat(result.getTestCases(), hasItem(case1));
    }

    @Test
    public void removesAllTestCasesWithSeveralStatuses() throws Exception {
        TestSuiteResult testSuiteResult = new TestSuiteResult();
        TestCaseResult case1 = new TestCaseResult().withStatus(Status.PASSED);
        TestCaseResult case2 = new TestCaseResult().withStatus(Status.PENDING);
        testSuiteResult.getTestCases().add(case1);
        testSuiteResult.getTestCases().add(case2);
        testSuiteResult.getTestCases().add(new TestCaseResult().withStatus(Status.CANCELED));
        testSuiteResult.getTestCases().add(new TestCaseResult().withStatus(Status.BROKEN));
        testSuiteResult.getTestCases().add(new TestCaseResult().withStatus(Status.FAILED));
        new TestCasesFilterStatusEvent(Arrays.asList(Status.PASSED, Status.PENDING))
                .process(testSuiteResult);
        assertThat(testSuiteResult.getTestCases(), hasSize(3));
        assertThat(testSuiteResult.getTestCases(), not(hasItem(case1)));
        assertThat(testSuiteResult.getTestCases(), not(hasItem(case2)));
    }
}