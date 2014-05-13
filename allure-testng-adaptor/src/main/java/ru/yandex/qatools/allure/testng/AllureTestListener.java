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
        String testName = getName(iTestResult);
        startedTestNames.add(testName);

        TestCaseStartedEvent event = new TestCaseStartedEvent(suiteUid, testName);
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
        if (!startedTestNames.contains(getName(iTestResult))) {
            onTestStart(iTestResult);
        }

        Throwable throwable = iTestResult.getThrowable();
        if (throwable == null) {
            throwable = new SkipException("The test was skipped for some reason");
        }

        getLifecycle().fire(new TestCaseCanceledEvent()
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

    private String getName(ITestResult iTestResult) {
        StringBuilder sb = new StringBuilder(iTestResult.getName());
        Object[] parameters = iTestResult.getParameters();
        if (parameters != null && parameters.length > 0) {
            sb.append("[");
            for (Object parameter: parameters) {
                sb.append(parameter).append(",");
            }
            sb.replace(sb.length() - 1, sb.length(), "]");
        }
        return sb.toString();
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
