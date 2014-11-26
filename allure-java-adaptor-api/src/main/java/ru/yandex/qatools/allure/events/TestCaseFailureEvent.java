package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Status;

/**
 * Using to change testCase status to {@link ru.yandex.qatools.allure.model.Status#FAILED} or
 * {@link ru.yandex.qatools.allure.model.Status#BROKEN}
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 *         <p/>
 * @see ru.yandex.qatools.allure.events.TestCaseStatusChangeEvent
 * @see ru.yandex.qatools.allure.Allure
 */
public class TestCaseFailureEvent extends TestCaseStatusChangeEvent {

    /**
     * Returns the status {@link ru.yandex.qatools.allure.model.Status#FAILED} if
     * throwable instance of {@link AssertionError} or
     * {@link ru.yandex.qatools.allure.model.Status#BROKEN} otherwise
     *
     * @return the status
     */
    @Override
    protected Status getStatus() {
        return throwable instanceof AssertionError ? Status.FAILED : Status.BROKEN;
    }

    /**
     * Returns default message (using in {@link ru.yandex.qatools.allure.events.TestCaseStatusChangeEvent}
     * if throwable not specified)
     *
     * @return default message
     */
    @Override
    protected String getMessage() {
        return getStatus() == Status.FAILED ? "Test failed with unknown reason" : "Test broken with unknown reason";
    }

    /**
     * Sets throwable using fluent-api
     *
     * @param value to set
     * @return modified instance
     */
    public TestCaseStatusChangeEvent withThrowable(Throwable value) {
        setThrowable(value);
        return this;
    }
}
