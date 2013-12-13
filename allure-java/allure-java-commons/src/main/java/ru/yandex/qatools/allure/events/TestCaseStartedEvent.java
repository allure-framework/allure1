package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.SeverityLevel;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.utils.AnnotationManager;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestCaseStartedEvent implements TestCaseEvent {

    private String suiteUid;
    private String name;
    private Collection<Annotation> annotations = new ArrayList<>();

    public TestCaseStartedEvent() {
    }

    @Override
    public void process(TestCaseResult testCase) {
        testCase.setStart(System.currentTimeMillis());
        testCase.setStatus(Status.PASSED);
        testCase.setSeverity(SeverityLevel.NORMAL);
        testCase.setName(name);

        AnnotationManager annotationsManager = new AnnotationManager(annotations);
        annotationsManager.isTitleAnnotationPresentUpdate(testCase);
        annotationsManager.isDescriptionAnnotationPresentUpdate(testCase);
        annotationsManager.isSeverityAnnotationPresentUpdate(testCase);
        annotationsManager.isStoryAnnotationPresentUpdate(testCase);
    }

    public String getSuiteUid() {
        return suiteUid;
    }

    public void setSuiteUid(String suiteUid) {
        this.suiteUid = suiteUid;
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

    public TestCaseStartedEvent withSuiteUid(String suiteUid) {
        setSuiteUid(suiteUid);
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
