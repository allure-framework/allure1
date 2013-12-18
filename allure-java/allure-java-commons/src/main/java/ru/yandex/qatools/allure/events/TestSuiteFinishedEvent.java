package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.TestSuiteResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestSuiteFinishedEvent extends AbstractTestSuiteFinishedEvent {

    public TestSuiteFinishedEvent(String uid) {
        setUid(uid);
    }

    @Override
    public void process(TestSuiteResult testSuite) {
        testSuite.setStop(System.currentTimeMillis());
    }

}
