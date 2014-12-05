package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.TestSuiteResult;

/**
 * Using to finish testSuite
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 *         <p/>
 * @see ru.yandex.qatools.allure.Allure
 */
public class TestSuiteFinishedEvent extends AbstractTestSuiteFinishedEvent {

    /**
     * Constructs an new event with specified uid
     *
     * @param uid initial uid
     */
    public TestSuiteFinishedEvent(String uid) {
        setUid(uid);
    }

    /**
     * Sets testSuite finish time
     *
     * @param testSuite to change
     */
    @Override
    public void process(TestSuiteResult testSuite) {
        testSuite.setStop(System.currentTimeMillis());
    }

}
