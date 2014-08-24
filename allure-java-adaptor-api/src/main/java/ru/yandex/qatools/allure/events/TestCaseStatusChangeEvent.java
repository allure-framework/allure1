package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;

import static ru.yandex.qatools.allure.utils.FailureUtils.createDefaultFailure;
import static ru.yandex.qatools.allure.utils.FailureUtils.createFailureFromThrowable;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.05.14
 *         <p/>
 *         Abstract TestCase status event
 * @see ru.yandex.qatools.allure.events.TestCaseFailureEvent
 * @see ru.yandex.qatools.allure.events.TestCaseCanceledEvent
 * @see ru.yandex.qatools.allure.events.TestCasePendingEvent
 */
public abstract class TestCaseStatusChangeEvent extends AbstractTestCaseStatusChangeEvent {

    /**
     * Returns the status, used in {@link #process(TestCaseResult)}
     * to change TestCaseResult status
     *
     * @return the status
     */
    protected abstract Status getStatus();

    /**
     * Returns default message, used if throwable not specified
     *
     * @return default message
     */
    protected abstract String getMessage();

    /**
     * Change status in specified testCase. If throwable not specified uses
     * {@link #getDefaultFailure()} and {@link #getFailure()} otherwise
     *
     * @param testCase to change
     */
    @Override
    public void process(TestCaseResult testCase) {
        testCase.setStatus(getStatus());
        testCase.setFailure(throwable == null ? createDefaultFailure(getMessage()) : createFailureFromThrowable(throwable));
    }
}
