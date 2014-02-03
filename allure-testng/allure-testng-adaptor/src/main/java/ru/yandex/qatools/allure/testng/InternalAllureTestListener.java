package ru.yandex.qatools.allure.testng;

import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.*;
import ru.yandex.qatools.allure.utils.AnnotationManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Internal listener to simplify unit testing. It processes all test case events.
 *
 * @author Kirill Kozlov kozlov.k.e@gmail.com
 *         Date: 04.02.14
 */
public class InternalAllureTestListener {
    private Allure lifecycle = Allure.LIFECYCLE;

    private String suiteUid;

    private Set<String> startedTestNames = Collections.newSetFromMap(
            new ConcurrentHashMap<String, Boolean>());

    public InternalAllureTestListener(String suiteUid) {
        this.suiteUid = suiteUid;
    }

    public void onTestStart(AllureTestResultAdaptor testResult) {
        startedTestNames.add(testResult.getName());

        TestCaseStartedEvent event = new TestCaseStartedEvent(suiteUid, testResult.getName());
        AnnotationManager am = new AnnotationManager(testResult.getAnnotations());

        am.update(event);

        getLifecycle().fire(event);
    }

    public void onTestSuccess(AllureTestResultAdaptor testResult) {
        fireFinishTest();
    }

    public void onTestFailure(AllureTestResultAdaptor testResult) {
        getLifecycle().fire(new TestCaseFailureEvent()
                .withThrowable(testResult.getThrowable())
        );
        fireFinishTest();
    }

    public void onTestSkipped(AllureTestResultAdaptor testResult) {
        if (!startedTestNames.contains(testResult.getName())) {
            onTestStart(testResult);
        }

        getLifecycle().fire(new TestCaseSkippedEvent()
                .withThrowable(testResult.getThrowable())
        );
        fireFinishTest();
    }

    public void onTestFailedButWithinSuccessPercentage(AllureTestResultAdaptor testResult) {
        getLifecycle().fire(new TestCaseFailureEvent()
                .withThrowable(testResult.getThrowable())
        );
        fireFinishTest();
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
}
