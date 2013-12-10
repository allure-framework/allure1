package ru.yandex.qatools.allure.storages;

import ru.yandex.qatools.allure.model.TestCaseResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.11.13
 */
public final class TestStorage {

    private static final Map<String, TestCaseResult> TEST_DATA = new ConcurrentHashMap<>();

    private TestStorage() {
    }

    private static void checkTest(String uid) {
        if (!TEST_DATA.containsKey(uid)) {
            TEST_DATA.put(uid, new TestCaseResult());
        }
    }

    public static TestCaseResult getTest(String uid) {
        checkTest(uid);
        return TEST_DATA.get(uid);
    }

    public static TestCaseResult pollTest(String uid) {
        checkTest(uid);
        return TEST_DATA.remove(uid);
    }

}
