package ru.yandex.qatools.allure.junit;

import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.TestCaseFailureEvent;
import ru.yandex.qatools.allure.events.TestCaseFinishedEvent;
import ru.yandex.qatools.allure.events.TestCaseSkippedEvent;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;

import java.util.UUID;

/**
 * @author Artem Eroshenko eroshenkoam
 *         5/21/13, 2:16 PM
 */

public class TestCaseReportRule extends TestWatcher {

    private String uid;

    private String runUid;

    public TestCaseReportRule(TestSuiteReportRule testSuite) {
        this.uid = UUID.randomUUID().toString();
        this.runUid = testSuite.getUid();
    }

    protected void starting(Description description) {
        Allure.LIFECYCLE.fire(new TestCaseStartedEvent()
                .withUid(uid)
                .withName(description.getMethodName())
                .withAnnotations(description.getAnnotations())
        );
    }

    protected void finished(Description description) {
        Allure.LIFECYCLE.fire(new TestCaseFinishedEvent().withUid(uid).withRunUid(runUid));
    }

    protected void skipped(AssumptionViolatedException e, Description description) {
        Allure.LIFECYCLE.fire(new TestCaseSkippedEvent().withThrowable(e));
    }

    protected void failed(Throwable e, Description description) {
        Allure.LIFECYCLE.fire(new TestCaseFailureEvent().withUid(uid).withThrowable(e));
    }

}
