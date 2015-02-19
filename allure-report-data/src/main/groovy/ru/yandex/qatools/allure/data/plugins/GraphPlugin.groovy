package ru.yandex.qatools.allure.data.plugins

import ru.yandex.qatools.allure.data.AllureGraph
import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.utils.PluginUtils

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.15
 */
class GraphPlugin implements ProcessPlugin<AllureTestCase> {

    public static final String GRAPH_JSON = "graph.json"

    AllureGraph graph = new AllureGraph();

    @Override
    void process(AllureTestCase testCase) {
        use(PluginUtils) {
            graph.testCases.add(testCase.toInfo());
        }
    }

    @Override
    List<PluginData> getPluginData() {
        return Arrays.asList(new PluginData(GRAPH_JSON, graph));
    }

    @Override
    Class<AllureTestCase> getType() {
        return AllureTestCase;
    }
}
