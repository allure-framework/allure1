package ru.yandex.qatools.allure.data.providers;

import ru.yandex.qatools.allure.data.AllureGraph;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.12.13
 */
public class GraphDataProvider extends AbstractDataProvider {

    private static final String TEST_RUN_TO_GRAPH_XSL = "xsl/testrun-to-graph.xsl";

    private static final String GRAPH_JSON = "graph.json";

    @Override
    public String[] getXslTransformations() {
        return new String[]{TEST_RUN_TO_GRAPH_XSL};
    }

    @Override
    public String getJsonFileName() {
        return GRAPH_JSON;
    }

    @Override
    public Class<?> getType() {
        return AllureGraph.class;
    }
}
