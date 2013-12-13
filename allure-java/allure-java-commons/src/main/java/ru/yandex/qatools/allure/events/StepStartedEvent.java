package ru.yandex.qatools.allure.events;


import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.Step;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class StepStartedEvent implements StepEvent {
    private String title;

    public StepStartedEvent() {
    }

    @Override
    public void process(Step step) {
        step.setTitle(title);
        step.setStatus(Status.PASSED);
        step.setStart(System.currentTimeMillis());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public StepStartedEvent withTitle(String title) {
        setTitle(title);
        return this;
    }

}
