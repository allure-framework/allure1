package ru.yandex.qatools.allure.events;

import org.apache.commons.lang3.exception.ExceptionUtils;
import ru.yandex.qatools.allure.model.Failure;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestCaseFailureEvent implements TestCaseEvent {
    private Throwable throwable;

    public TestCaseFailureEvent() {
    }

    @Override
    public void process(TestCaseResult testCase) {
        if (throwable instanceof AssertionError) {
            testCase.setStatus(Status.FAILED);
        } else {
            testCase.setStatus(Status.BROKEN);
        }

        testCase.setFailure(getFailure());
    }

    private Failure getFailure() {
        return new Failure()
                .withMessage(ExceptionUtils.getMessage(getThrowable()))
                .withStackTrace(ExceptionUtils.getStackTrace(getThrowable()));
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable e) {
        this.throwable = e;
    }

    public TestCaseFailureEvent withThrowable(Throwable throwable) {
        setThrowable(throwable);
        return this;
    }
}
