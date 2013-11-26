package ru.yandex.qatools.allure.events;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestAssumptionFailureEvent implements Event {
    private String uid;
    private Throwable throwable;

    public TestAssumptionFailureEvent(String uid, Throwable e) {
        this.uid = uid;
        this.throwable = e;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
