package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestCaseFinishedEvent implements TestCaseEvent {
    private String runUid;
    private String uid;

    public TestCaseFinishedEvent() {
    }

    @Override
    public void process(TestCaseResult testCase) {
        testCase.setStop(System.currentTimeMillis());
    }

    public String getRunUid() {
        return runUid;
    }

    public void setRunUid(String runUid) {
        this.runUid = runUid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public TestCaseFinishedEvent withRunUid(String runUid) {
        setRunUid(runUid);
        return this;
    }

    public TestCaseFinishedEvent withUid(String uid) {
        setUid(uid);
        return this;
    }

}
