package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.Step;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 *         <p/>
 *         Using to change step status to {@link ru.yandex.qatools.allure.model.Status#CANCELED}
 */
public class StepCanceledEvent extends AbstractStepCanceledEvent {

    /**
     * Change specified step status to {@link ru.yandex.qatools.allure.model.Status#CANCELED}
     *
     * @param step which will be changed
     */
    @Override
    public void process(Step step) {
        step.setStatus(Status.CANCELED);
    }

}
