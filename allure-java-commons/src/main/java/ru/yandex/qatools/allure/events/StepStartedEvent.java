package ru.yandex.qatools.allure.events;


import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.Step;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class StepStartedEvent extends AbstractStepStartedEvent {

    public StepStartedEvent(String name) {
        setName(name);
    }

    @Override
    public void process(Step step) {
        step.setName(getName());
        step.setStatus(Status.PASSED);
        step.setStart(System.currentTimeMillis());
        step.setTitle(getTitle());
    }

}
