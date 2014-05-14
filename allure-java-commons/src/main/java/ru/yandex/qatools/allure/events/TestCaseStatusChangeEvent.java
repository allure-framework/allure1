package ru.yandex.qatools.allure.events;

import org.apache.commons.lang3.exception.ExceptionUtils;
import ru.yandex.qatools.allure.model.Failure;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.05.14
 */
public abstract class TestCaseStatusChangeEvent extends AbstractTestCaseStatusChangeEvent {

    protected abstract Status getStatus();

    protected abstract String getMessage();

    @Override
    public void process(TestCaseResult testCase) {
        testCase.setStatus(getStatus());
        testCase.setFailure(throwable == null ? getDefaultFailure() : getFailure());
    }

    private Failure getFailure() {
        return new Failure()
                .withMessage(ExceptionUtils.getMessage(getThrowable()))
                .withStackTrace(ExceptionUtils.getStackTrace(getThrowable()));
    }

    private Failure getDefaultFailure() {
        return new Failure()
                .withMessage(getMessage())
                .withStackTrace("There are no stack trace");
    }
}
