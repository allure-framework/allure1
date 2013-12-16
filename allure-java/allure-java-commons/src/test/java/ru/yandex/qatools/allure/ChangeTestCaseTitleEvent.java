package ru.yandex.qatools.allure;

import ru.yandex.qatools.allure.events.TestCaseEvent;
import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 15.12.13
 */
public class ChangeTestCaseTitleEvent implements TestCaseEvent {
    private String title;

    public ChangeTestCaseTitleEvent(String title) {
        this.title = title;
    }

    @Override
    public void process(TestCaseResult context) {
        context.setTitle(title);
    }
}
