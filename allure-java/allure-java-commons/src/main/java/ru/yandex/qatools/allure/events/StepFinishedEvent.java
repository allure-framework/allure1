package ru.yandex.qatools.allure.events;


import ru.yandex.qatools.allure.model.Step;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class StepFinishedEvent extends AbstractStepFinishedEvent {

    @Override
    public void process(Step step) {
        step.setStop(System.currentTimeMillis());
    }
}
