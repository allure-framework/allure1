package ru.yandex.qatools.allure.storages;

import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.12.13
 */
public class TestSuiteStorage {

    private final Map<String, TestSuiteResult> testSuiteData = new ConcurrentHashMap<>();

    public TestSuiteResult get(String uid) {
        if (!testSuiteData.containsKey(uid)) {
            testSuiteData.put(uid, new TestSuiteResult());
        }
        return testSuiteData.get(uid);
    }

    public void remove(String uid) {
        testSuiteData.remove(uid);
    }
}
