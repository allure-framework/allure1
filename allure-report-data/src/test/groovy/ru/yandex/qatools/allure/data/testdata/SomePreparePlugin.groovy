package ru.yandex.qatools.allure.data.testdata

import ru.yandex.qatools.allure.data.plugins.PreparePlugin

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 18.02.15
 */
class SomePreparePlugin implements PreparePlugin<Integer> {
    @Override
    void prepare(Integer data) {
    }

    @Override
    Class<Integer> getType() {
        return null
    }
}
