package ru.yandex.qatools.allure.data.plugins;

import ru.yandex.qatools.allure.data.AllureTestCase;

/**
 * Base class for all tab plugins.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 17.04.15
 * @see ProcessPlugin
 */
public abstract class TabPlugin extends PluginWithResources<AllureTestCase> {

    @Override
    public Class<AllureTestCase> getType() {
        return AllureTestCase.class;
    }
}
