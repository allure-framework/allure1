package ru.yandex.qatools.allure.junit;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.TestRunFinishedEvent;
import ru.yandex.qatools.allure.events.TestRunStartedEvent;

import java.util.UUID;


/**
 * @author Artem Eroshenko eroshenkoam
 *         5/21/13, 2:06 PM
 */
public class TestSuiteReportRule extends TestWatcher {

	private String uid;

	public TestSuiteReportRule() {
	}

	protected void starting(Description description) {
		uid = UUID.randomUUID().toString();
        Allure.LIFECYCLE.fire(new TestRunStartedEvent(
                uid,
                description.getTestClass().getName(),
                description.getAnnotations()
        ));
	}

	protected void finished(Description description) {
        Allure.LIFECYCLE.fire(new TestRunFinishedEvent(uid));
	}

	public String getUid() {
		return uid;
	}
}