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
    private String suiteUid;

    public AllureTestListener() {
        suiteUid = UUID.randomUUID().toString();
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        Allure.LIFECYCLE.fire(new TestCaseStartedEvent(suiteUid, iTestResult.getName())
                .withAnnotations(Arrays.asList(iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotations()))
        );
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        Allure.LIFECYCLE.fire(new TestCaseFinishedEvent()
        );
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        Allure.LIFECYCLE.fire(new TestCaseFailureEvent()
                .withThrowable(iTestResult.getThrowable())
        );
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        Allure.LIFECYCLE.fire(new TestCaseSkippedEvent()
                .withThrowable(iTestResult.getThrowable())
        );
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        Allure.LIFECYCLE.fire(new TestCaseFailureEvent()
                .withThrowable(iTestResult.getThrowable())
        );
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        Allure.LIFECYCLE.fire(new TestSuiteStartedEvent(suiteUid, iTestContext.getCurrentXmlTest().getSuite().getName())
                .withAnnotations(Collections.<Annotation>emptyList())
        );
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        Allure.LIFECYCLE.fire(new TestSuiteFinishedEvent(suiteUid)
        );
    }
}
