package ru.yandex.qatools.allure.experimental.testdata;

import ru.yandex.qatools.allure.events.ClearStepStorageEvent;
import ru.yandex.qatools.allure.events.ClearTestStorageEvent;
import ru.yandex.qatools.allure.events.StepEvent;
import ru.yandex.qatools.allure.events.StepFinishedEvent;
import ru.yandex.qatools.allure.events.StepStartedEvent;
import ru.yandex.qatools.allure.events.TestCaseEvent;
import ru.yandex.qatools.allure.events.TestCaseFinishedEvent;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;
import ru.yandex.qatools.allure.events.TestSuiteEvent;
import ru.yandex.qatools.allure.events.TestSuiteFinishedEvent;
import ru.yandex.qatools.allure.experimental.LifecycleListener;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 04.06.14
 */
public class SimpleListener extends LifecycleListener {

    private final HashMap<EventType, AtomicInteger> counts = new HashMap<>();

    public SimpleListener() {
        for (EventType type : EventType.values()) {
            counts.put(type, new AtomicInteger(0));
        }
    }

    @Override
    public void fire(StepStartedEvent event) {
        counts.get(EventType.STEP_STARTED_EVENT).incrementAndGet();
    }

    @Override
    public void fire(StepEvent event) {
        counts.get(EventType.STEP_EVENT).incrementAndGet();
    }

    @Override
    public void fire(StepFinishedEvent event) {
        counts.get(EventType.STEP_FINISHED_EVENT).incrementAndGet();
    }

    @Override
    public void fire(TestCaseStartedEvent event) {
        counts.get(EventType.TESTCASE_STARTED_EVENT).incrementAndGet();
    }

    @Override
    public void fire(TestCaseEvent event) {
        counts.get(EventType.TESTCASE_EVENT).incrementAndGet();
    }

    @Override
    public void fire(TestCaseFinishedEvent event) {
        counts.get(EventType.TESTCASE_FINISHED_EVENT).incrementAndGet();
    }

    @Override
    public void fire(TestSuiteEvent event) {
        counts.get(EventType.TESTSUITE_EVENT).incrementAndGet();
    }

    @Override
    public void fire(TestSuiteFinishedEvent event) {
        counts.get(EventType.TESTSUITE_FINISHED_EVENT).incrementAndGet();
    }

    @Override
    public void fire(ClearStepStorageEvent event) {
        counts.get(EventType.CLEAR_STEP_STORAGE_EVENT).incrementAndGet();
    }

    @Override
    public void fire(ClearTestStorageEvent event) {
        counts.get(EventType.CLEAR_TEST_STORAGE_EVENT).incrementAndGet();
    }

    public int get(EventType eventType) {
        return counts.containsKey(eventType) ? counts.get(eventType).get() : 0;
    }

    public enum EventType {
        STEP_STARTED_EVENT,
        STEP_EVENT,
        STEP_FINISHED_EVENT,
        TESTCASE_STARTED_EVENT,
        TESTCASE_EVENT,
        TESTCASE_FINISHED_EVENT,
        TESTSUITE_EVENT,
        TESTSUITE_FINISHED_EVENT,
        CLEAR_STEP_STORAGE_EVENT,
        CLEAR_TEST_STORAGE_EVENT
    }
}
