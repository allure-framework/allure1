package ru.yandex.qatools.allure;

import ru.yandex.qatools.allure.config.AllureConfig;
import ru.yandex.qatools.allure.events.*;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.Step;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.allure.storages.StepStorage;
import ru.yandex.qatools.allure.storages.TestCaseStorage;
import ru.yandex.qatools.allure.storages.TestSuiteStorage;

import java.util.Iterator;

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
        //init root step in parent thread if needed
        stepStorage.get();

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

        if (Status.PASSED.equals(testCase.getStatus())) {
            new RemoveAttachmentsEvent(AllureConfig.newInstance().getRemoveAttachments()).process(root);
        }

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

        if (AllureConfig.newInstance().isRemovePassedTests()) {
            Iterator<TestCaseResult> iterator = testSuite.getTestCases().iterator();
            while (iterator.hasNext()) {
                TestCaseResult testCase = iterator.next();
                if (Status.PASSED.equals(testCase.getStatus())) {
                    iterator.remove();
                }
            }

            if (!testSuite.getTestCases().isEmpty()) {
                writeTestSuiteResult(testSuite);
            }
            return;
        }

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

    public String getVersion() {
        return AllureConfig.newInstance().getVersion();
    }
}
