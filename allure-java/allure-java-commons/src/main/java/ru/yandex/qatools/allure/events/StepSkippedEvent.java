package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.Step;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class StepSkippedEvent implements StepEvent {
    private Throwable throwable;

    public StepSkippedEvent() {
    }

    @Override
    public void process(Step step) {
        step.setStatus(Status.SKIPPED);
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public StepSkippedEvent withThrowable(Throwable throwable) {
        setThrowable(throwable);
        return this;
    }

}
