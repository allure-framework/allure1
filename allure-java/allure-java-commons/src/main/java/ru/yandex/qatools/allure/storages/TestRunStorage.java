package ru.yandex.qatools.allure.storages;

import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.11.13
 */
public final class TestRunStorage {

    private static final Map<String, TestSuiteResult> TEST_RUN_DATA = new ConcurrentHashMap<>();

    private TestRunStorage() {
    }

    private static void checkTestRun(String uid) {
        if (!TEST_RUN_DATA.containsKey(uid)) {
            TEST_RUN_DATA.put(uid, new TestSuiteResult());
        }
    }

    public static TestSuiteResult getTestRun(String uid) {
        checkTestRun(uid);
        return TEST_RUN_DATA.get(uid);
    }

    public static TestSuiteResult pollTestRun(String uid) {
        checkTestRun(uid);
        return TEST_RUN_DATA.remove(uid);
    }
}
