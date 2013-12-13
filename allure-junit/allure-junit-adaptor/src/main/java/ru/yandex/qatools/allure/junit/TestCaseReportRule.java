package ru.yandex.qatools.allure.junit;

import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.TestCaseFailureEvent;
import ru.yandex.qatools.allure.events.TestCaseFinishedEvent;
import ru.yandex.qatools.allure.events.TestCaseSkippedEvent;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;

/**
 * @author Artem Eroshenko eroshenkoam
 *         5/21/13, 2:16 PM
 */

public class TestCaseReportRule extends TestWatcher {

    private String runUid;

    public TestCaseReportRule(TestSuiteReportRule testSuite) {
        this.runUid = testSuite.getUid();
    }

    protected void starting(Description description) {
        Allure.LIFECYCLE.fire(new TestCaseStartedEvent()
                .withName(description.getMethodName())
                .withAnnotations(description.getAnnotations())
                .withSuiteUid(runUid)
        );
    }

    protected void finished(Description description) {
        Allure.LIFECYCLE.fire(new TestCaseFinishedEvent());
    }

    protected void skipped(AssumptionViolatedException e, Description description) {
        Allure.LIFECYCLE.fire(new TestCaseSkippedEvent().withThrowable(e));
    }

    protected void failed(Throwable e, Description description) {
        Allure.LIFECYCLE.fire(new TestCaseFailureEvent().withThrowable(e));
    }

}
