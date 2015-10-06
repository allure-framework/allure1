package ru.yandex.qatools.allure.testdata

import ru.yandex.qatools.allure.plugins.PluginData
import ru.yandex.qatools.allure.plugins.WithData
import ru.yandex.qatools.allure.plugins.ProcessPlugin

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 18.02.15
 */
class SomeProcessPlugin implements ProcessPlugin, WithData {
    @Override
    void process(Object data) {
    }

    @Override
    List<PluginData> getPluginData() {
        return null
    }

    @Override
    Class getType() {
        return null
    }
}
