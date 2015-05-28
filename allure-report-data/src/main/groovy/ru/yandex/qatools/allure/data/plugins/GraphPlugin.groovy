package ru.yandex.qatools.allure.data.plugins

import ru.yandex.qatools.allure.data.AllureGraph
import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.utils.PluginUtils

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.15
 */
@Plugin.Name("graph")
@Plugin.Priority(300)
class GraphPlugin extends DefaultTabPlugin {

    @Plugin.Data
    AllureGraph graph = new AllureGraph();

    @Override
    void process(AllureTestCase testCase) {
        use(PluginUtils) {
            graph.testCases.add(testCase.toInfo());
        }
    }
}
