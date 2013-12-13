package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestCaseFinishedEvent implements TestCaseEvent {
    private String suiteUid;
    private String uid;

    public TestCaseFinishedEvent() {
    }

    @Override
    public void process(TestCaseResult testCase) {
        testCase.setStop(System.currentTimeMillis());
    }

    public String getSuiteUid() {
        return suiteUid;
    }

    public void setSuiteUid(String suiteUid) {
        this.suiteUid = suiteUid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public TestCaseFinishedEvent withRunUid(String runUid) {
        setSuiteUid(runUid);
        return this;
    }

    public TestCaseFinishedEvent withUid(String uid) {
        setUid(uid);
        return this;
    }

}
