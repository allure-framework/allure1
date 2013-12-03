package ru.yandex.qatools.allure.data.json;

import ru.yandex.qatools.allure.data.AllureGraph;
import ru.yandex.qatools.allure.data.utils.AllureReportUtils;

import java.io.File;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 02.12.13
 */
public class GraphData implements JSONObject {

    private static final String FILE_NAME = "graph.json";

    private AllureGraph allureGraph;

    public GraphData(AllureGraph allureGraph) {
        this.allureGraph = allureGraph;
    }

    @Override
    public void serialize(File outputDirectory) {
        AllureReportUtils.serialize(outputDirectory, FILE_NAME, allureGraph);
    }
}
