package ru.yandex.qatools.allure.testdata

import ru.yandex.qatools.allure.plugins.PreparePlugin

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
