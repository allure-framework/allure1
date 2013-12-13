package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.TestSuiteResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestSuiteFinishedEvent implements TestSuiteEvent {
    private String uid;

    public TestSuiteFinishedEvent() {
    }

    @Override
    public void process(TestSuiteResult testSuite) {
        testSuite.setStop(System.currentTimeMillis());
    }

    @Override
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public TestSuiteFinishedEvent withUid(String uid) {
        setUid(uid);
        return this;
    }

}
