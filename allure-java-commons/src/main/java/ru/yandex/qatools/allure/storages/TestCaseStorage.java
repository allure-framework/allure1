package ru.yandex.qatools.allure.storages;

import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.12.13
 *         <p/>
 *         Using to storage information about current testCase context
 * @see ru.yandex.qatools.allure.Allure
 */
public class TestCaseStorage extends ThreadLocal<TestCaseResult> {

    /**
     * Returns the current thread's "initial value". Construct an new
     * {@link ru.yandex.qatools.allure.model.TestCaseResult}
     *
     * @return the initial value for this thread-local
     */
    @Override
    protected TestCaseResult initialValue() {
        return new TestCaseResult();
    }
}
