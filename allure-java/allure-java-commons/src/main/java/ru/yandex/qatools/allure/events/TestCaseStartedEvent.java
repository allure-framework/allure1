package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestCaseStartedEvent implements TestSuiteEvent {
    private String uid;
    private String name;
    private Collection<Annotation> annotations;

    public TestCaseStartedEvent() {
    }

    @Override
    public void process(TestSuiteResult testCase) {
        testCase.setStart(System.currentTimeMillis());
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(Collection<Annotation> annotations) {
        this.annotations = annotations;
    }

    public TestCaseStartedEvent withUid(String uid) {
        setUid(uid);
        return this;
    }

    public TestCaseStartedEvent withName(String name) {
        setName(name);
        return this;
    }

    public TestCaseStartedEvent withAnnotations(Collection<Annotation> annotations) {
        setAnnotations(annotations);
        return this;
    }
}
