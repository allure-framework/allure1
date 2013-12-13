package ru.yandex.qatools.allure;

import ru.yandex.qatools.allure.events.*;
import ru.yandex.qatools.allure.model.*;
import ru.yandex.qatools.allure.model.Step;
import ru.yandex.qatools.allure.storages.*;

import static ru.yandex.qatools.allure.utils.AllureWriteUtils.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public enum Allure {

    LIFECYCLE;

    private static final StepStorage STEP_STORAGE = new StepStorage();

    private static final TestCaseStorage TEST_CASE_STORAGE = new TestCaseStorage();

    private static final TestSuiteStorage TEST_SUITE_STORAGE = new TestSuiteStorage();

    private static final Object LOCK = new Object();

    private Allure() {
    }

    public void fire(StepStartedEvent event) {
        Step step = new Step();
        event.process(step);
        STEP_STORAGE.put(step);
    }

    public void fire(StepEvent event) {
        Step step = STEP_STORAGE.pollLast();
        event.process(step);
    }

    public void fire(StepFinishedEvent event) {
        Step step = STEP_STORAGE.pollLast();
        event.process(step);
        STEP_STORAGE.getLast().getSteps().add(step);
    }

    public void fire(TestCaseEvent event) {
        TestCaseResult testCase = TEST_CASE_STORAGE.get();
        event.process(testCase);
    }

    public void fire(TestCaseFinishedEvent event) {
        TestCaseResult testCase = TEST_CASE_STORAGE.get();
        event.process(testCase);
        synchronized (LOCK) {
            TEST_SUITE_STORAGE.get(event.getSuiteUid()).getTestCases().add(testCase);
        }
        TEST_CASE_STORAGE.remove();
    }


    public void fire(TestSuiteEvent event) {
        TestSuiteResult testSuite = TEST_SUITE_STORAGE.get(event.getUid());
        event.process(testSuite);
    }

    public void fire(TestSuiteFinishedEvent event) {
        String suiteUid = event.getUid();
        TestSuiteResult testSuite = TEST_SUITE_STORAGE.get(suiteUid);
        event.process(testSuite);
        marshalTestSuite(testSuite);
        TEST_SUITE_STORAGE.remove(suiteUid);
    }
}
