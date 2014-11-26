package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Status;

/**
 * Using to change testCase status to {@link ru.yandex.qatools.allure.model.Status#PENDING}
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 *         <p/>
 * @see ru.yandex.qatools.allure.events.TestCaseStatusChangeEvent
 * @see ru.yandex.qatools.allure.Allure
 */
public class TestCasePendingEvent extends TestCaseStatusChangeEvent {

    private String message = "Test not implemented yet";

    /**
     * Returns the status {@link ru.yandex.qatools.allure.model.Status#PENDING}
     *
     * @return the status
     */
    @Override
    protected Status getStatus() {
        return Status.PENDING;
    }

    /**
     * Returns default message (using in {@link ru.yandex.qatools.allure.events.TestCaseStatusChangeEvent}
     * if throwable not specified)
     *
     * @return default message
     */
    @Override
    protected String getMessage() {
        return message;
    }

    /**
     * Sets the default message
     *
     * @param message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Sets message using fluent-api
     *
     * @param message to set
     * @return modified instance
     */
    public TestCasePendingEvent withMessage(String message) {
        setMessage(message);
        return this;
    }

    /**
     * Sets throwable using fluent-api
     *
     * @param value to set
     * @return modified instance
     */
    public TestCasePendingEvent withThrowable(Throwable value) {
        setThrowable(value);
        return this;
    }
}
