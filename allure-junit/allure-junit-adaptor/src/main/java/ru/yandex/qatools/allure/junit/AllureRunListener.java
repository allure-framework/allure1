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

    private static final Map<String, String> SUITES = new HashMap<>();

    @Override
    public void testStarted(Description description) {
        System.out.println("testStarted");
        String suiteUid = getSuiteUid(description);

        TestCaseStartedEvent event = new TestCaseStartedEvent(suiteUid, description.getMethodName());
        AnnotationManager am = new AnnotationManager(description.getAnnotations());

        am.update(event);

        Allure.LIFECYCLE.fire(event);
    }

    @Override
    public void testFinished(Description description) {
        System.out.println("testFinished");
        Allure.LIFECYCLE.fire(new TestCaseFinishedEvent());
    }

    @Override
    public void testFailure(Failure failure) {
        System.out.println("testFailure");
        Allure.LIFECYCLE.fire(new TestCaseFailureEvent().withThrowable(failure.getException()));
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        System.out.println("testAssumptionFailure");
        Allure.LIFECYCLE.fire(new TestCaseSkippedEvent().withThrowable(failure.getException()));
    }

    @Override
    public void testIgnored(Description description) {
        System.out.println("testIgnored");
        //if test class annotated with @Ignored
        if (description.getMethodName() == null) {
            return;
        }

        testStarted(description);
        Allure.LIFECYCLE.fire(new TestCaseSkippedEvent().withThrowable(new Exception("Test ignored.")));
        testFinished(description);
    }

    @Override
    public void testRunFinished(Result result) {
        System.out.println("testRunFinished");
        for (String uid : SUITES.values()) {
            testSuiteFinished(uid);
        }
    }

    public void testSuiteStarted(String uid, String suiteName, Collection<Annotation> annotations) {
        System.out.println("testSuiteStarted");
        TestSuiteStartedEvent event = new TestSuiteStartedEvent(uid, suiteName);
        AnnotationManager am = new AnnotationManager(annotations);

        am.update(event);

        Allure.LIFECYCLE.fire(event);
    }

    public void testSuiteFinished(String uid) {
        System.out.println("testSuiteFinished");
        Allure.LIFECYCLE.fire(new TestSuiteFinishedEvent(uid));
    }


    private String getSuiteUid(Description description) {
        String suiteName = description.getClassName();
        Collection<Annotation> annotations = Arrays.asList(description.getTestClass().getAnnotations());
        if (!SUITES.containsKey(suiteName)) {
            String uid = UUID.randomUUID().toString();
            testSuiteStarted(uid, suiteName, annotations);
            SUITES.put(suiteName, uid);
            return uid;
        }
        return SUITES.get(suiteName);
    }

    public Allure getLifecycle() {
        return lifecycle;
    }

    public void setLifecycle(Allure lifecycle) {
        this.lifecycle = lifecycle;
    }
}
