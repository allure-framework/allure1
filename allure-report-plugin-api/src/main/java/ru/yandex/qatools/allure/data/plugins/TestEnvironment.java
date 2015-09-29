package ru.yandex.qatools.allure.data.plugins;

import java.util.Map;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 29.09.15
 */
public interface TestEnvironment {

    /**
     * Returns the name of the test run.
     */
    String getTestRunName();

    /**
     * Returns the url to test run in test execution system.
     */
    String getTestRunUrl();

    /**
     * Returns the test run environment.
     */
    Map<String, String> getEnvironment();
}
