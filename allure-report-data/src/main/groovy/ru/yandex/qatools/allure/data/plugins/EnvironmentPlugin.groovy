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
        if (data.id) {
            environment.id = data.id
            environment.name = data.name
        };
        environment.parameter.addAll(data.parameter)
    }

    @Override
    Class<Environment> getType() {
        Environment
    }

    @Override
    Widget getWidget() {
        def widget = new KeyValueWidget(name)
        widget.data = environment.parameter.take(10).collect {
            new KeyValueWidgetItem(key: it.key, value: it.value)
        }.sort { it.key }
        widget
    }
}
