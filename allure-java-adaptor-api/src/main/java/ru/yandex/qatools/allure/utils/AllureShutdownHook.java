package ru.yandex.qatools.allure.utils;

import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.TestSuiteFinishedEvent;
import ru.yandex.qatools.allure.model.Failure;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.Map;
import java.util.Set;

/**
 * If test execution was interrupted this hook can help to save test data.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 08.12.14
 */
public class AllureShutdownHook implements Runnable {

    private final Set<Map.Entry<String, TestSuiteResult>> testSuites;

    /**
     * Create a new instance of shutdown hook.
     */
    public AllureShutdownHook(Set<Map.Entry<String, TestSuiteResult>> testSuites) {
        this.testSuites = testSuites;
    }

    /**
     * Mark unfinished test cases as interrupted for each unfinished test suite, then write
     * test suite result
     * @see #createFakeTestcaseWithWarning(ru.yandex.qatools.allure.model.TestSuiteResult)
     * @see #markTestcaseAsInterruptedIfNotFinishedYet(ru.yandex.qatools.allure.model.TestCaseResult)
     */
    @Override
    public void run() {
        for (Map.Entry<String, TestSuiteResult> entry : testSuites) {
            for (TestCaseResult testCase : entry.getValue().getTestCases()) {
                markTestcaseAsInterruptedIfNotFinishedYet(testCase);
            }
            entry.getValue().getTestCases().add(createFakeTestcaseWithWarning(entry.getValue()));

            Allure.LIFECYCLE.fire(new TestSuiteFinishedEvent(entry.getKey()));
        }
    }

    /**
     * Create fake test case, which will used for mark suite as interrupted.
     */
    public TestCaseResult createFakeTestcaseWithWarning(TestSuiteResult testSuite) {
        return new TestCaseResult()
                .withName(testSuite.getName())
                .withTitle(testSuite.getName())
                .withStart(testSuite.getStart())
                .withStop(System.currentTimeMillis())
                .withFailure(new Failure().withMessage("Test suite was interrupted, some test cases may be lost"))
                .withStatus(Status.BROKEN);
    }

    /**
     * If test not finished yet (in our case if stop time is zero) mark it as interrupted.
     * Set message, stop time and status.
     */
    public void markTestcaseAsInterruptedIfNotFinishedYet(TestCaseResult testCase) {
        if (testCase.getStop() == 0L) {
            testCase.setStop(System.currentTimeMillis());
            testCase.setStatus(Status.BROKEN);
            testCase.setFailure(new Failure().withMessage("Test was interrupted"));
        }
    }
}
