package ru.yandex.qatools.allure.junit;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.*;
import ru.yandex.qatools.allure.utils.AnnotationManager;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 20.12.13
 */
@SuppressWarnings("unused")
public class AllureRunListener extends RunListener {

    private Allure lifecycle = Allure.LIFECYCLE;

    private final Map<String, String> suites = new HashMap<>();

    @Override
    public void testStarted(Description description) {
        String suiteUid = getSuiteUid(description);

        TestCaseStartedEvent event = new TestCaseStartedEvent(suiteUid, description.getMethodName());
        AnnotationManager am = new AnnotationManager(description.getAnnotations());

        am.update(event);

        getLifecycle().fire(event);
    }

    @Override
    public void testFinished(Description description) {
        getLifecycle().fire(new TestCaseFinishedEvent());
    }

    @Override
    public void testFailure(Failure failure) {
        getLifecycle().fire(new TestCaseFailureEvent().withThrowable(failure.getException()));
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        getLifecycle().fire(new TestCaseSkippedEvent().withThrowable(failure.getException()));
    }

    @Override
    public void testIgnored(Description description) {
        //if test class annotated with @Ignored do nothing
        if (description.getMethodName() == null) {
            return;
        }

        testStarted(description);
        getLifecycle().fire(new TestCaseSkippedEvent().withThrowable(new Exception("Test ignored.")));
        testFinished(description);
    }

    @Override
    public void testRunFinished(Result result) {
        for (String uid : getSuites().values()) {
            testSuiteFinished(uid);
        }
    }

    public void testSuiteStarted(String uid, String suiteName, Collection<Annotation> annotations) {
        TestSuiteStartedEvent event = new TestSuiteStartedEvent(uid, suiteName);
        AnnotationManager am = new AnnotationManager(annotations);

        am.update(event);

        getLifecycle().fire(event);
    }

    public void testSuiteFinished(String uid) {
        getLifecycle().fire(new TestSuiteFinishedEvent(uid));
    }


    public String getSuiteUid(Description description) {
        String suiteName = description.getClassName();
        if (!getSuites().containsKey(suiteName)) {
            String uid = UUID.randomUUID().toString();

            Collection<Annotation> annotations = Arrays.asList(description.getTestClass().getAnnotations());
            testSuiteStarted(uid, suiteName, annotations);

            getSuites().put(suiteName, uid);
        }
        return getSuites().get(suiteName);
    }

    public Allure getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(Allure lifecycle) {
        this.lifecycle = lifecycle;
    }

    public Map<String, String> getSuites() {
        return suites;
    }
}
