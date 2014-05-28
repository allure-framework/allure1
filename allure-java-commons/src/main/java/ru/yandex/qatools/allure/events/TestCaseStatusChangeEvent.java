package ru.yandex.qatools.allure.events;

import org.apache.commons.lang3.exception.ExceptionUtils;
import ru.yandex.qatools.allure.model.Failure;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.05.14
 *
 *         Abstract TestCase status event
 *
 *         @see ru.yandex.qatools.allure.events.TestCaseFailureEvent
 *         @see ru.yandex.qatools.allure.events.TestCaseCanceledEvent
 *         @see ru.yandex.qatools.allure.events.TestCasePendingEvent
 */
public abstract class TestCaseStatusChangeEvent extends AbstractTestCaseStatusChangeEvent {

    /**
     * Returns the status, used in {@link #process(TestCaseResult)}
     * to change TestCaseResult status
     * @return the status
     */
    protected abstract Status getStatus();

    /**
     * Returns default message, used if throwable not specified
     * @return default message
     */
    protected abstract String getMessage();

    /**
     * Change status in specified testCase. If throwable not specified uses
     * {@link #getDefaultFailure()} and {@link #getFailure()} otherwise
     * @param testCase to change
     */
    @Override
    public void process(TestCaseResult testCase) {
        testCase.setStatus(getStatus());
        testCase.setFailure(throwable == null ? getDefaultFailure() : getFailure());
    }

    /**
     * Create failure from throwable using {@link org.apache.commons.lang3.exception.ExceptionUtils}
     * @return created failure
     */
    private Failure getFailure() {
        return new Failure()
                .withMessage(ExceptionUtils.getMessage(getThrowable()))
                .withStackTrace(ExceptionUtils.getStackTrace(getThrowable()));
    }

    /**
     * Create default failure (if throwable not specified)
     * @return default failure
     */
    private Failure getDefaultFailure() {
        return new Failure()
                .withMessage(getMessage())
                .withStackTrace("There are no stack trace");
    }
}
