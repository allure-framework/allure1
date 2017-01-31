package ru.yandex.qatools.allure.junit;

import org.junit.Ignore;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.events.ClearStepStorageEvent;
import ru.yandex.qatools.allure.events.TestCaseCanceledEvent;
import ru.yandex.qatools.allure.events.TestCaseFailureEvent;
import ru.yandex.qatools.allure.events.TestCaseFinishedEvent;
import ru.yandex.qatools.allure.events.TestCasePendingEvent;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;
import ru.yandex.qatools.allure.events.TestSuiteFinishedEvent;
import ru.yandex.qatools.allure.events.TestSuiteStartedEvent;
import ru.yandex.qatools.allure.utils.AnnotationManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 20.12.13
 */
public class AllureRunListener extends RunListener {

    private Allure lifecycle = Allure.LIFECYCLE;

    private final Map<String, String> suites = new HashMap<>();

    public void testSuiteStarted(Description description) {
        String uid = generateSuiteUid(description.getClassName());

        TestSuiteStartedEvent event = new TestSuiteStartedEvent(uid, description.getClassName());
        AnnotationManager am = new AnnotationManager(description.getAnnotations());

        am.update(event);

        event.withLabels(AllureModelUtils.createTestFrameworkLabel("JUnit"));

        getLifecycle().fire(event);
    }

    @Override
    public void testStarted(Description description) {
        TestCaseStartedEvent event = new TestCaseStartedEvent(getSuiteUid(description), description.getMethodName());
        AnnotationManager am = new AnnotationManager(description.getAnnotations());

        am.update(event);

        fireClearStepStorage();
        getLifecycle().fire(event);
    }

    @Override
    public void testFailure(Failure failure) {
        if (failure.getDescription().isTest()) {
            fireTestCaseFailure(failure.getException());
        } else {
            startFakeTestCase(failure.getDescription());
            fireTestCaseFailure(failure.getException());
            finishFakeTestCase();
        }
    }

    @Override
    public void testAssumptionFailure(Failure failure) {
        testFailure(failure);
    }

    @Override
    public void testIgnored(Description description) {
        startFakeTestCase(description);
        getLifecycle().fire(new TestCasePendingEvent().withMessage(getIgnoredMessage(description)));
        finishFakeTestCase();
    }

    @Override
    public void testFinished(Description description) {
        getLifecycle().fire(new TestCaseFinishedEvent());
    }


    public void testSuiteFinished(String uid) {
        getLifecycle().fire(new TestSuiteFinishedEvent(uid));
    }

    @Override
    public void testRunFinished(Result result) {
        for (String uid : getSuites().values()) {
            testSuiteFinished(uid);
        }
    }

    public String generateSuiteUid(String suiteName) {
        String uid = UUID.randomUUID().toString();
        synchronized (getSuites()) {
            getSuites().put(suiteName, uid);
        }
        return uid;
    }

    public String getSuiteUid(Description description) {
        String suiteName = description.getClassName();
        if (!getSuites().containsKey(suiteName)) {
            Class testClass = description.getTestClass();
            Description suiteDescription;
            if (testClass != null) {
                suiteDescription = Description.createSuiteDescription(testClass);
            } else {
                suiteDescription = Description.createSuiteDescription(description.getClassName());
            }
            testSuiteStarted(suiteDescription);
        }
        return getSuites().get(suiteName);
    }

    public String getIgnoredMessage(Description description) {
        Ignore ignore = description.getAnnotation(Ignore.class);
        return ignore == null || ignore.value().isEmpty() ? "Test ignored (without reason)!" : ignore.value();
    }

    public void startFakeTestCase(Description description) {
        String uid = getSuiteUid(description);

        String name = description.isTest() ? description.getMethodName() : description.getClassName();
        TestCaseStartedEvent event = new TestCaseStartedEvent(uid, name);
        AnnotationManager am = new AnnotationManager(description.getAnnotations());
        am.update(event);

        fireClearStepStorage();
        getLifecycle().fire(event);
    }

    public void finishFakeTestCase() {
        getLifecycle().fire(new TestCaseFinishedEvent());
    }

    public void fireTestCaseFailure(Throwable throwable) {
        if (throwable instanceof AssumptionViolatedException) {
            getLifecycle().fire(new TestCaseCanceledEvent().withThrowable(throwable));
        } else {
            getLifecycle().fire(new TestCaseFailureEvent().withThrowable(throwable));
        }
    }

    public void fireClearStepStorage() {
        getLifecycle().fire(new ClearStepStorageEvent());
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
