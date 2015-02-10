package ru.yandex.qatools.allure.data.plugins;

import ru.yandex.qatools.allure.data.AllureTestCase;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 09.02.15
 */
public interface Plugin {

    void process(AllureTestCase testCase);
}
