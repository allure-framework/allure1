package ru.yandex.qatools.allure.events;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestRunStartedEvent implements Event {
    private String uid;
    private String testRunName;
    private Collection<Annotation> annotations;

    public TestRunStartedEvent(String uid, String testRunName, Collection<Annotation> annotations) {
        this.uid = uid;
        this.testRunName = testRunName;
        this.annotations = annotations;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTestRunName() {
        return testRunName;
    }

    public void setTestRunName(String testRunName) {
        this.testRunName = testRunName;
    }

    public Collection<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Collection<Annotation> annotations) {
        this.annotations = annotations;
    }
}
