package ru.yandex.qatools.allure.data.plugins;

import ru.yandex.qatools.allure.data.AllureTestCase;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 17.04.15
 */
public abstract class TabPlugin implements ProcessPlugin<AllureTestCase> {

    @Override
    public Class<AllureTestCase> getType() {
        return AllureTestCase.class;
    }
}
