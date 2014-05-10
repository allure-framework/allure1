package ru.yandex.qatools.allure;

import ru.yandex.qatools.allure.events.TestSuiteEvent;
import ru.yandex.qatools.allure.model.TestSuiteResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 15.12.13
 */
public class ChangeTestSuiteTitleEvent implements TestSuiteEvent {
    private String uid;
    private String title;

    public ChangeTestSuiteTitleEvent(String uid, String title) {
        this.uid = uid;
        this.title = title;
    }

    @Override
    public String getUid() {
        return uid;
    }

    @Override
    public void process(TestSuiteResult context) {
        context.setTitle(title);
    }
}
