package ru.yandex.qatools.allure.events;


import ru.yandex.qatools.allure.model.Step;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 *         <p/>
 *         Uisng to mark step finished
 * @see ru.yandex.qatools.allure.Allure
 */
public class StepFinishedEvent extends AbstractStepFinishedEvent {

    /**
     * Sets stop time to specified step
     *
     * @param step which will be changed
     */
    @Override
    public void process(Step step) {
        step.setStop(System.currentTimeMillis());
    }
}
