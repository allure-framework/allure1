package ru.yandex.qatools.allure.data.testdata

import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.plugins.Plugin
import ru.yandex.qatools.allure.data.plugins.DefaultTabPlugin

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 22.04.15
 */
@Plugin.Name("somePluginWithResources")
class SomePluginWithResources extends DefaultTabPlugin {

    @Override
    void process(AllureTestCase data) {
        //do nothing
    }
}
