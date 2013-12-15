package ru.yandex.qatools.allure.testng;

import org.testng.*;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 22.11.13
 */
@SuppressWarnings("unused")
public class AllureTestListener implements ITestListener {
    private String runUid;

    private Set<String> startedTestUids =
            Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    public AllureTestListener() {
        runUid = UUID.randomUUID().toString();
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        String testUid = getTestUid(iTestResult);
        startedTestUids.add(testUid);
        Allure.LIFECYCLE.fire(new TestStartedEvent(
                testUid,
                iTestResult.getName(),
                Arrays.asList(iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotations())
        ));
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        fireTestFinishedEvent(iTestResult);
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        Allure.LIFECYCLE.fire(new TestFailureEvent(
                getTestUid(iTestResult),
                iTestResult.getThrowable()
        ));
        fireTestFinishedEvent(iTestResult);
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        String testUid = getTestUid(iTestResult);
        if (!startedTestUids.contains(testUid)) {
            onTestStart(iTestResult);
        }
        Throwable skipReason = iTestResult.getThrowable();
        if (skipReason == null) {
            skipReason = new SkipException("The test was skipped for some reasons");
        }
        Allure.LIFECYCLE.fire(new TestAssumptionFailureEvent(
                testUid,
                skipReason
        ));
        fireTestFinishedEvent(iTestResult);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        Allure.LIFECYCLE.fire(new TestFailureEvent(
                getTestUid(iTestResult),
                iTestResult.getThrowable()
        ));
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        Allure.LIFECYCLE.fire(new TestRunStartedEvent(
                runUid,
                iTestContext.getCurrentXmlTest().getSuite().getName(),
                Collections.<Annotation>emptyList()
        ));
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        Allure.LIFECYCLE.fire(new TestRunFinishedEvent(
                runUid
        ));
    }

    private void fireTestFinishedEvent(ITestResult iTestResult) {
        Allure.LIFECYCLE.fire(new TestFinishedEvent(
                runUid,
                getTestUid(iTestResult)
        ));
    }

    private String getTestUid(ITestResult iTestResult) {
        return Integer.toHexString(iTestResult.hashCode());
    }
}
