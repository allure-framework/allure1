package ru.yandex.qatools.allure.events;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestFinishedEvent implements Event {
    private String runUid;
    private String uid;

    public TestFinishedEvent(String runUid, String uid) {
        this.runUid = runUid;
        this.uid = uid;
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
}
