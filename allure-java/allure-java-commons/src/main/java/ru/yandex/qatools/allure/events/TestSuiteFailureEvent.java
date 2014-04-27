package ru.yandex.qatools.allure.events;

import org.apache.commons.lang3.exception.ExceptionUtils;
import ru.yandex.qatools.allure.model.Failure;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestSuiteResult;

/**
 * @author Kirill Merkushev lanwen@yandex.ru
 *         Date: 27.04.14
 */
public class TestSuiteFailureEvent extends AbstractTestSuiteFailureEvent {

    public TestSuiteFailureEvent(String uid) {
        setUid(uid);
    }

    @Override
    public void process(TestSuiteResult testSuite) {
        Status status = throwable instanceof AssertionError ? Status.FAILED : Status.BROKEN;
        testSuite.setStatus(status);
        testSuite.setFailure(getFailure());
    }


    private Failure getFailure() {
        return new Failure()
                .withMessage(ExceptionUtils.getMessage(getThrowable()))
                .withStackTrace(ExceptionUtils.getStackTrace(getThrowable()));
    }


}
