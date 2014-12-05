package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.Step;

/**
 * Using to mark current step as failed and attach for it {@link Throwable}
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 *         <p/>
 */
public class StepFailureEvent extends AbstractStepFailureEvent {

    /**
     * Change step status to {@link ru.yandex.qatools.allure.model.Status#FAILED} if
     * throwable instance of AssertionError and to {@link ru.yandex.qatools.allure.model.Status#BROKEN}
     * otherwise
     *
     * @param step which will be changed
     */
    @Override
    public void process(Step step) {
        Status status = throwable instanceof AssertionError ? Status.FAILED : Status.BROKEN;
        step.setStatus(status);
    }

    /**
     * Set specified throwable to event using fluent-api interface
     *
     * @param throwable the throwable to set
     * @return modified instance
     */
    public StepFailureEvent withThrowable(Throwable throwable) {
        setThrowable(throwable);
        return this;
    }

}
