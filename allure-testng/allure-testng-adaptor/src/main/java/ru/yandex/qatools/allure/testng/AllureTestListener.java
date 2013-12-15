package ru.yandex.qatools.allure.testng;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 22.11.13
 */
@SuppressWarnings("unused")
public class AllureTestListener implements ITestListener {
    private String runUid;

    public AllureTestListener() {
        runUid = UUID.randomUUID().toString();
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        Allure.LIFECYCLE.fire(new TestStartedEvent(
                getTestUid(iTestResult),
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
        Allure.LIFECYCLE.fire(new TestAssumptionFailureEvent(
                getTestUid(iTestResult),
                iTestResult.getThrowable()
        ));
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
