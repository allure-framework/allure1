package ru.yandex.qatools.allure.events;


import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.Step;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class StepStartedEvent implements StepEvent {
    private String name;

    public StepStartedEvent() {
    }

    @Override
    public void process(Step step) {
        step.setName(name);
        step.setStatus(Status.PASSED);
        step.setStart(System.currentTimeMillis());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StepStartedEvent withName(String name) {
        setName(name);
        return this;
    }

}
