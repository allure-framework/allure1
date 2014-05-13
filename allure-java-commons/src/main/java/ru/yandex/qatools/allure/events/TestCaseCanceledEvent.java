package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Status;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestCaseCanceledEvent extends TestCaseStatusChangeEvent {

    @Override
    protected Status getStatus() {
        return Status.CANCELED;
    }

    @Override
    protected String getMessage() {
        return "Test skipped with unknown reason";
    }

    @Override
    public TestCaseCanceledEvent withThrowable(Throwable value) {
        setThrowable(value);
        return this;
    }
}
