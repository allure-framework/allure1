package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Status;

/**
 * Using to change testCase status to {@link ru.yandex.qatools.allure.model.Status#CANCELED}
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 *         <p/>
 * @see ru.yandex.qatools.allure.events.TestCaseStatusChangeEvent
 * @see ru.yandex.qatools.allure.Allure
 */
public class TestCaseCanceledEvent extends TestCaseStatusChangeEvent {

    /**
     * Returns the status {@link ru.yandex.qatools.allure.model.Status#CANCELED}
     *
     * @return the status {@link ru.yandex.qatools.allure.model.Status#CANCELED}
     */
    @Override
    protected Status getStatus() {
        return Status.CANCELED;
    }

    /**
     * Returns default message (using in {@link ru.yandex.qatools.allure.events.TestCaseStatusChangeEvent}
     * if throwable not specified)
     *
     * @return default message
     */
    @Override
    protected String getMessage() {
        return "Test skipped with unknown reason";
    }

    /**
     * Sets throwable using fluent-api
     *
     * @param value to set
     * @return modified instance
     */
    public TestCaseCanceledEvent withThrowable(Throwable value) {
        setThrowable(value);
        return this;
    }
}
