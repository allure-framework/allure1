package ru.yandex.qatools.allure.data.plugins

import com.google.inject.Inject
import ru.yandex.qatools.allure.data.KeyValueWidgetItem

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 18.02.15
 */
@Plugin.Name("environment")
@Plugin.Priority(100)
class EnvironmentPlugin extends AbstractPlugin implements WithWidget {

    @Inject
    Environment environment

    @Override
    Object getWidgetData() {
        environment.parameters.collect {
            new KeyValueWidgetItem(key: it.key, value: it.value)
        }.sort { it.key }
    }
}
