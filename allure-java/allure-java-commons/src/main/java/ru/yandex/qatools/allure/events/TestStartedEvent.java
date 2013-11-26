package ru.yandex.qatools.allure.events;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestStartedEvent implements Event {
    private String uid;
    private String testName;
    private Collection<Annotation> annotations;

    public TestStartedEvent(String uid, String testName, Collection<Annotation> annotations) {
        this.uid = uid;
        this.testName = testName;
        this.annotations = annotations;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Collection<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Collection<Annotation> annotations) {
        this.annotations = annotations;
    }
}
