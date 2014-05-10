package ru.yandex.qatools.allure.data.providers;

import ru.yandex.qatools.allure.data.AllureGraph;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.StringReader;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.serialize;
import static ru.yandex.qatools.allure.data.utils.XslTransformationUtils.applyTransformation;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.12.13
 */
public class GraphDataProvider implements DataProvider {

    private static final String TEST_RUN_TO_GRAPH_XSL = "xsl/testrun-to-graph.xsl";

    private static final String GRAPH_JSON = "graph.json";

    @Override
    public void provide(String testPack, File outputDirectory) {
        String allureGraphBody = applyTransformation(testPack, TEST_RUN_TO_GRAPH_XSL);

        AllureGraph allureGraph = JAXB.unmarshal(
                new StringReader(allureGraphBody),
                AllureGraph.class
        );

        serialize(outputDirectory, GRAPH_JSON, allureGraph);
    }
}
