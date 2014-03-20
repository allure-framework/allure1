package ru.yandex.qatools.allure;

import ru.yandex.qatools.allure.events.*;
import ru.yandex.qatools.allure.model.*;
import ru.yandex.qatools.allure.model.Step;
import ru.yandex.qatools.allure.storages.*;

import static ru.yandex.qatools.allure.utils.AllureResultsUtils.writeTestSuiteResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class Allure {

    public static final Allure LIFECYCLE = new Allure();

    private final StepStorage stepStorage = new StepStorage();

    private final TestCaseStorage testCaseStorage = new TestCaseStorage();

    private final TestSuiteStorage testSuiteStorage = new TestSuiteStorage();

    private final Object lock = new Object();

    private Allure() {
    }

    public void fire(StepStartedEvent event) {
        Step step = new Step();
        event.process(step);
        stepStorage.put(step);
    }

    public void fire(StepEvent event) {
        Step step = stepStorage.getLast();
        event.process(step);
    }

    public void fire(StepFinishedEvent event) {
        Step step = stepStorage.pollLast();
        event.process(step);
        stepStorage.getLast().getSteps().add(step);
    }

    public void fire(TestCaseStartedEvent event) {
        TestCaseResult testCase = testCaseStorage.get();
        event.process(testCase);

        synchronized (lock) {
            testSuiteStorage.get(event.getSuiteUid()).getTestCases().add(testCase);
        }
    }

    public void fire(TestCaseEvent event) {
        TestCaseResult testCase = testCaseStorage.get();
        event.process(testCase);
    }

    public void fire(TestCaseFinishedEvent event) {
        TestCaseResult testCase = testCaseStorage.get();
        event.process(testCase);

        Step root = stepStorage.pollLast();
        testCase.getSteps().addAll(root.getSteps());
        testCase.getAttachments().addAll(root.getAttachments());

        stepStorage.remove();
        testCaseStorage.remove();
    }


    public void fire(TestSuiteEvent event) {
        TestSuiteResult testSuite = testSuiteStorage.get(event.getUid());
        event.process(testSuite);
    }

    public void fire(TestSuiteFinishedEvent event) {
        String suiteUid = event.getUid();
        TestSuiteResult testSuite = testSuiteStorage.get(suiteUid);
        event.process(testSuite);
        testSuiteStorage.remove(suiteUid);

        writeTestSuiteResult(testSuite);
    }

    public void fire(ClearStepStorageEvent event) {
        stepStorage.remove();
    }

    public void fire(ClearTestStorageEvent event) {
        testCaseStorage.remove();
    }

    public StepStorage getStepStorage() {
        return stepStorage;
    }

    public TestCaseStorage getTestCaseStorage() {
        return testCaseStorage;
    }

    public TestSuiteStorage getTestSuiteStorage() {
        return testSuiteStorage;
    }
}
