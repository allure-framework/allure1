package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.12.13
 *         <p/>
 *         Implement this interface to allow access to current testCase context.
 *         Usage see at {@link ru.yandex.qatools.allure.Allure}
 * @see ru.yandex.qatools.allure.events.Event
 */
public interface TestCaseEvent extends Event<TestCaseResult> {
}
