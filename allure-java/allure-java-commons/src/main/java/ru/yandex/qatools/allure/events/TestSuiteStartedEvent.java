package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.allure.utils.AnnotationManager;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestSuiteStartedEvent implements TestSuiteEvent {
    private String uid;
    private String name;
    private Collection<Annotation> annotations;

    public TestSuiteStartedEvent() {
    }

    @Override
    public void process(TestSuiteResult testSuite) {
        testSuite.setStart(System.currentTimeMillis());
        testSuite.setName(name);

        AnnotationManager annotationsManager = new AnnotationManager(annotations);
        annotationsManager.isTitleAnnotationPresentUpdate(testSuite);
        annotationsManager.isDescriptionAnnotationPresentUpdate(testSuite);
        annotationsManager.isStoryAnnotationPresentUpdate(testSuite);
    }

    @Override
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

    public TestSuiteStartedEvent withUid(String uid) {
        setUid(uid);
        return this;
    }

    public TestSuiteStartedEvent withName(String name) {
        setName(name);
        return this;
    }

    public TestSuiteStartedEvent withAnnotations(Collection<Annotation> annotations) {
        setAnnotations(annotations);
        return this;
    }

}
