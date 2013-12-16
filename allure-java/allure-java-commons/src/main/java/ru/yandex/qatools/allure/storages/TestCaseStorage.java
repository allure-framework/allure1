package ru.yandex.qatools.allure.storages;

import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.12.13
 */
public class TestCaseStorage extends ThreadLocal<TestCaseResult> {
    @Override
    protected TestCaseResult initialValue() {
        return new TestCaseResult();
    }
}
