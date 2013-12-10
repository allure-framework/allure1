package ru.yandex.qatools.allure.storages;

import ru.yandex.qatools.allure.model.Step;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.11.13
 */
public final class TestStepStorage {

    private static final Map<Thread, Deque<Step>> TEST_STEP_DATA = new ConcurrentHashMap<>();

    private TestStepStorage() {
    }

    private static void checkStep() {
        Thread currentThread = Thread.currentThread();
        if (!TEST_STEP_DATA.containsKey(currentThread)) {
            TEST_STEP_DATA.put(currentThread, new LinkedList<Step>());
        }

        if (TEST_STEP_DATA.get(currentThread).isEmpty()) {
            TEST_STEP_DATA.get(currentThread).add(new Step());
        }
    }

    public static Step getTestStep() {
        checkStep();
        return TEST_STEP_DATA.get(Thread.currentThread()).getLast();
    }

    public static void putTestStep(Step testStepResult) {
        checkStep();
        TEST_STEP_DATA.get(Thread.currentThread()).add(testStepResult);
    }

    public static Step pollTestStep() {
        checkStep();
        return TEST_STEP_DATA.get(Thread.currentThread()).pollLast();
    }
}
