package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestCaseFinishedEvent implements TestCaseEvent {
    public TestCaseFinishedEvent() {
    }

    @Override
    public void process(TestCaseResult testCase) {
        testCase.setStop(System.currentTimeMillis());
    }
}
