package ru.yandex.qatools.allure.junit;

import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.TestCaseFailureEvent;
import ru.yandex.qatools.allure.events.TestCaseFinishedEvent;
import ru.yandex.qatools.allure.events.TestCaseSkippedEvent;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;
import ru.yandex.qatools.allure.utils.AnnotationManager;

/**
 * @author Artem Eroshenko eroshenkoam
 *         5/21/13, 2:16 PM
 */

public class TestCaseReportRule extends TestWatcher {

    private String suiteUid;

    public TestCaseReportRule(TestSuiteReportRule testSuite) {
        this.suiteUid = testSuite.getUid();
    }

    protected void starting(Description description) {
        TestCaseStartedEvent event = new TestCaseStartedEvent(suiteUid, description.getMethodName());
        AnnotationManager am = new AnnotationManager(description.getAnnotations());

        am.update(event);

        Allure.LIFECYCLE.fire(event);
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
