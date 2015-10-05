package ru.yandex.qatools.allure.data.plugins

import ru.yandex.qatools.allure.data.KeyValueWidgetItem
import ru.yandex.qatools.commons.model.Environment

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 18.02.15
 */
@Plugin.Name("environment")
@Plugin.Priority(100)
class EnvironmentPlugin extends AbstractPlugin implements ProcessPlugin<Environment>, WithWidget {

    @Plugin.Data
    Environment environment = new Environment(
            id: UUID.randomUUID().toString(), name: "Allure Test Pack");

    @Override
    void process(Environment data) {
        environment.id = data.id ?: environment.id
        environment.name = data.name ?: environment.name
        environment.url = data.url ?: environment.url

        for (def parameter : data.parameter) {
            if (!environment.parameter.count { it.key == parameter.key }) {
                environment.parameter.add(parameter)
            }

        }
    }

    @Override
    Class<Environment> getType() {
        Environment
    }

    @Override
    Object getWidgetData() {
        environment.parameter.collect {
            new KeyValueWidgetItem(key: it.key, value: it.value)
        }.sort { it.key }
    }
}
