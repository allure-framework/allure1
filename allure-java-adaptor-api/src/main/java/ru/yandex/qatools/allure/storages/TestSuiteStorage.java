package ru.yandex.qatools.allure.storages;

import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Using to storage information about current testCase context
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.12.13
 *         <p/>
 * @see ru.yandex.qatools.allure.Allure
 */

public class TestSuiteStorage {

    private final Map<String, TestSuiteResult> testSuiteData = new ConcurrentHashMap<>();

    /**
     * Returns the value in the current copy of variable from
     * {@link #testSuiteData}. If the variable has no value for the uid
     * will be created new {@link ru.yandex.qatools.allure.model.TestSuiteResult}
     *
     * @param uid using as key for {@link #testSuiteData} to find variable
     * @return testSuite context for specified uid
     */
    public TestSuiteResult get(String uid) {
        if (!testSuiteData.containsKey(uid)) {
            testSuiteData.put(uid, new TestSuiteResult());
        }
        return testSuiteData.get(uid);
    }

    /**
     * Remove variable by uid from {@link #testSuiteData}
     *
     * @param uid to remove
     */
    public TestSuiteResult remove(String uid) {
        return testSuiteData.remove(uid);
    }

    /**
     * Return all started suites
     */
    public Set<Map.Entry<String, TestSuiteResult>> getStartedSuites() {
        return testSuiteData.entrySet();
    }
}
