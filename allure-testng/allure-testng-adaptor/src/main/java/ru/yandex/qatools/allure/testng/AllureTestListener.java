package ru.yandex.qatools.allure.testng;

import org.testng.*;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.*;
import ru.yandex.qatools.allure.utils.AnnotationManager;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Allure framework listener for <a href="http://testng.org">TestNG</a> framework.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 22.11.13
 */
public class AllureTestListener implements ITestListener {

    private Allure lifecycle = Allure.LIFECYCLE;

    private String suiteUid = UUID.randomUUID().toString();

    private Set<String> startedTestNames = Collections.newSetFromMap(
            new ConcurrentHashMap<String, Boolean>());

    @Override
    public void onStart(ITestContext iTestContext) {
        getLifecycle().fire(new TestSuiteStartedEvent(
                suiteUid, iTestContext.getCurrentXmlTest().getSuite().getName())
        );
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        getLifecycle().fire(new TestSuiteFinishedEvent(suiteUid));
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        startedTestNames.add(iTestResult.getName());

        TestCaseStartedEvent event = new TestCaseStartedEvent(suiteUid, iTestResult.getName());
        AnnotationManager am = new AnnotationManager(getMethodAnnotations(iTestResult));

        am.update(event);

        getLifecycle().fire(event);
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        fireFinishTest();
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        getLifecycle().fire(new TestCaseFailureEvent()
                .withThrowable(iTestResult.getThrowable())
        );
        fireFinishTest();
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        if (!startedTestNames.contains(iTestResult.getName())) {
            onTestStart(iTestResult);
        }

        Throwable throwable = iTestResult.getThrowable();
        if (throwable == null) {
            throwable = new SkipException("The test was skipped for some reason");
        }

        getLifecycle().fire(new TestCaseSkippedEvent()
                .withThrowable(throwable)
        );
        fireFinishTest();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        getLifecycle().fire(new TestCaseFailureEvent()
                .withThrowable(iTestResult.getThrowable())
        );
        fireFinishTest();
    }

    public Annotation[] getMethodAnnotations(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotations();
    }

    private void fireFinishTest() {
        getLifecycle().fire(new TestCaseFinishedEvent());
    }

    Allure getLifecycle() {
        return lifecycle;
    }

    void setLifecycle(Allure lifecycle) {
        this.lifecycle = lifecycle;
    }

    String getSuiteUid() {
        return suiteUid;
    }
}
