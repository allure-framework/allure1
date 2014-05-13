package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Status;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestCasePendingEvent extends TestCaseStatusChangeEvent {

    private String message = "Test not implemented yet";

    @Override
    protected Status getStatus() {
        return Status.PENDING;
    }

    @Override
    protected String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public TestCasePendingEvent withMessage(String message) {
        setMessage(message);
        return this;
    }
}
