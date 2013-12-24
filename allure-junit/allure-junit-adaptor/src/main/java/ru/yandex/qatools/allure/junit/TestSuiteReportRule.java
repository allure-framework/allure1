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

    private Allure lifecycle = Allure.LIFECYCLE;

    private String uid;

    public TestSuiteReportRule() {
    }

    protected void starting(Description description) {
        uid = UUID.randomUUID().toString();
        TestSuiteStartedEvent event = new TestSuiteStartedEvent(uid, description.getClassName());
        AnnotationManager am = new AnnotationManager(description.getAnnotations());

        am.update(event);

        getLifecycle().fire(event);
    }

    protected void finished(Description description) {
        getLifecycle().fire(new TestSuiteFinishedEvent(uid));
    }

    public String getUid() {
        return uid;
    }

    public Allure getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(Allure lifecycle) {
        this.lifecycle = lifecycle;
    }
}