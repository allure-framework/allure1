package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * Uisng to mark testCase finished
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 *         <p/>
 * @see ru.yandex.qatools.allure.Allure
 */
public class TestCaseFinishedEvent extends AbstractTestCaseFinishedEvent {

    /**
     * Sets stop time to specified testCase
     *
     * @param testCase which will be changed
     */
    @Override
    public void process(TestCaseResult testCase) {
        testCase.setStop(System.currentTimeMillis());
    }
}
