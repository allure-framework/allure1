package ru.yandex.qatools.allure.data.plugins

import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.commons.model.Environment

import static ru.yandex.qatools.allure.data.utils.TextUtils.generateUid

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.02.15
 */
class EnvironmentPlugin implements TabPlugin {

    @Override
    void process(AllureTestCase testCase) {
    }

    @Override
    TabData getTabData() {
        return new TabData("environment.json", new Environment(
                id: generateUid(),
                name: "Allure Test Pack"
        ));
    }
}
