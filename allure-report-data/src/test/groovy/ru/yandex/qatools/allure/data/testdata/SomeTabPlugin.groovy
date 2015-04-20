package ru.yandex.qatools.allure.data.testdata

import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.plugins.Plugin
import ru.yandex.qatools.allure.data.plugins.PluginData
import ru.yandex.qatools.allure.data.plugins.TabPlugin

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 20.04.15
 */
@Plugin.Name('some-tab-plugin')
class SomeTabPlugin extends TabPlugin {

    public static final PLUGIN_DATA_NAME = 'some-tab-plugin.json'

    @Plugin.Data
    List<String> names = []

    @Override
    void process(AllureTestCase data) {
        names.add(data.name)
    }

    @Override
    List<PluginData> getPluginData() {
        return [new PluginData(PLUGIN_DATA_NAME, names)]
    }
}
