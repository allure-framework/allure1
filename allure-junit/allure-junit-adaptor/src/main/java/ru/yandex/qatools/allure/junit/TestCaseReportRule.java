package ru.yandex.qatools.allure.junit;

import org.junit.internal.AssumptionViolatedException;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.TestAssumptionFailureEvent;
import ru.yandex.qatools.allure.events.TestFailureEvent;
import ru.yandex.qatools.allure.events.TestFinishedEvent;
import ru.yandex.qatools.allure.events.TestStartedEvent;

import java.util.UUID;

import static ru.yandex.qatools.allure.utils.AllureWriteUtils.generateUid;

/**
 * @author Artem Eroshenko eroshenkoam
 *         5/21/13, 2:16 PM
 */

public class TestCaseReportRule extends TestWatcher {

	private String uid;

	private String runUid;

	public TestCaseReportRule(TestSuiteReportRule testSuite) {
        this.uid = generateUid(UUID.randomUUID().toString());
		this.runUid = testSuite.getUid();
	}

	protected void starting(Description description) {
        Allure.LIFECYCLE.fire(new TestStartedEvent(uid, description.getMethodName(), description.getAnnotations()));
	}

	protected void finished(Description description) {
        Allure.LIFECYCLE.fire(new TestFinishedEvent(runUid, uid));
	}

	protected void skipped(AssumptionViolatedException e, Description description) {
        Allure.LIFECYCLE.fire(new TestAssumptionFailureEvent(uid, e));
	}

	protected void failed(Throwable e, Description description) {
        Allure.LIFECYCLE.fire(new TestFailureEvent(uid, e));
	}

}
