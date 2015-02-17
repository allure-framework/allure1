package ru.yandex.qatools.allure.data.plugins

import ru.yandex.qatools.commons.model.Environment

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 18.02.15
 */
class EnvironmentPlugin implements ProcessPlugin<Environment> {

    Environment environment;

    @Override
    void process(Environment data) {
        if (data.id) {
            environment.id = data.id
            environment.name = data.name
        };
        environment.parameter.addAll(data.parameter)
    }

    @Override
    List<PluginData> getPluginData() {
        new PluginData("environment.json", environment)
    }

    @Override
    Class<Environment> getType() {
        Environment
    }
}
