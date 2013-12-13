package ru.yandex.qatools.allure.events;

import org.apache.commons.lang3.exception.ExceptionUtils;
import ru.yandex.qatools.allure.model.Failure;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestCaseSkippedEvent implements TestCaseEvent {
    private Throwable throwable;

    public TestCaseSkippedEvent() {
    }

    @Override
    public void process(TestCaseResult testCase) {
        testCase.setStatus(Status.SKIPPED);
        testCase.setFailure(getFailure());
    }

    private Failure getFailure() {
        Failure failure = new Failure();
        failure.setMessage(ExceptionUtils.getMessage(throwable));
        failure.setStackTrace(ExceptionUtils.getStackTrace(throwable));
        return failure;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable e) {
        this.throwable = e;
    }

    public TestCaseSkippedEvent withThrowable(Throwable throwable) {
        setThrowable(throwable);
        return this;
    }
}
