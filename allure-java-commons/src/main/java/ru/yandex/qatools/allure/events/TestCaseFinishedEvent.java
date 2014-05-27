package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 *         <p/>
 *         Uisng to mark testCase finished
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
