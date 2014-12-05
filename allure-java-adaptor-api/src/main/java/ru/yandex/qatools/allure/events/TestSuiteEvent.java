package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.TestSuiteResult;

/**
 * Implement this interface to allow access to current testSuite context.
 * Usage see at {@link ru.yandex.qatools.allure.Allure}
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.12.13
 *         <p/>
 * @see ru.yandex.qatools.allure.events.Event
 */
public interface TestSuiteEvent extends Event<TestSuiteResult> {
    String getUid();
}
