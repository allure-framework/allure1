package ru.yandex.qatools.allure.data.plugins

import ru.yandex.qatools.allure.data.AllureGraph
import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.utils.PluginUtils

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.serialize

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.15
 */
class Graph implements TabPlugin {

    AllureGraph graph = new AllureGraph();

    @Override
    void process(AllureTestCase testCase) {
        use(PluginUtils) {
            graph.testCases.add(testCase.toInfo());
        }
    }

    @Override
    TabData getTabData() {
        return new TabData("graph.json", graph);
    }
}
