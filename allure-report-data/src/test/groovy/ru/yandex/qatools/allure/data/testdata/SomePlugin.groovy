package ru.yandex.qatools.allure.data.testdata

import ru.yandex.qatools.allure.data.plugins.Plugin

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 18.02.15
 */
class SomePlugin implements Plugin<String>{
    @Override
    Class<String> getType() {
        return null
    }
}
