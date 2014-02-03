package ru.yandex.qatools.allure.testng;

import org.testng.*;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.*;

import java.util.UUID;

/**
 * Process only suite events, all test case events are simply passed to
 * @{link InternalAllureTestListener} with wrapped @{link ITestResult} argument
 *
 * @see InternalAllureTestListener
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 22.11.13
 */
public class AllureTestListener implements ITestListener {

    private Allure lifecycle = Allure.LIFECYCLE;

    private String suiteUid = UUID.randomUUID().toString();

    private InternalAllureTestListener internalAllureTestListener;

    public AllureTestListener() {
        internalAllureTestListener = new InternalAllureTestListener(suiteUid);
    }

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
        AllureTestResultAdaptor extendedTestResult = new AllureTestResultAdaptor(iTestResult);
        internalAllureTestListener.onTestStart(extendedTestResult);
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        AllureTestResultAdaptor extendedTestResult = new AllureTestResultAdaptor(iTestResult);
        internalAllureTestListener.onTestSuccess(extendedTestResult);
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        AllureTestResultAdaptor extendedTestResult = new AllureTestResultAdaptor(iTestResult);
        internalAllureTestListener.onTestFailure(extendedTestResult);
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        AllureTestResultAdaptor extendedTestResult = new AllureTestResultAdaptor(iTestResult);
        internalAllureTestListener.onTestSkipped(extendedTestResult);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        AllureTestResultAdaptor extendedTestResult = new AllureTestResultAdaptor(iTestResult);
        internalAllureTestListener.onTestFailedButWithinSuccessPercentage(extendedTestResult);
    }

    Allure getLifecycle() {
        return lifecycle;
    }
}
