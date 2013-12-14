package ru.yandex.qatools.allure.junit;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.TestSuiteFinishedEvent;
import ru.yandex.qatools.allure.events.TestSuiteStartedEvent;
import ru.yandex.qatools.allure.utils.AnnotationManager;

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
        TestSuiteStartedEvent event = new TestSuiteStartedEvent(uid, description.getTestClass().getName());
        AnnotationManager am = new AnnotationManager(description.getAnnotations());

        am.update(event);

        Allure.LIFECYCLE.fire(event);
    }

    protected void finished(Description description) {
        Allure.LIFECYCLE.fire(new TestSuiteFinishedEvent(uid));
    }

    public String getUid() {
        return uid;
    }
}