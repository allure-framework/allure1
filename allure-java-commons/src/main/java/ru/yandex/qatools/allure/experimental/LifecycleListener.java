package ru.yandex.qatools.allure.experimental;

import ru.yandex.qatools.allure.events.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 26.05.14
 */
public abstract class LifecycleListener {

    public void fire(StepStartedEvent event) {
    }

    public void fire(StepEvent event) {
    }

    public void fire(StepFinishedEvent event) {
    }

    public void fire(TestCaseStartedEvent event) {
    }

    public void fire(TestCaseEvent event) {
    }

    public void fire(TestCaseFinishedEvent event) {
    }

    public void fire(TestSuiteEvent event) {
    }

    public void fire(TestSuiteFinishedEvent event) {
    }

    public void fire(ClearStepStorageEvent event) {
    }

    public void fire(ClearTestStorageEvent event) {
    }
}
