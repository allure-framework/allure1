package ru.yandex.qatools.allure.data.transform;

import ru.yandex.qatools.allure.data.AllureGraph;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.StringReader;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.serialize;
import static ru.yandex.qatools.allure.data.utils.XslTransformationUtil.applyTransformation;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.12.13
 */
public class GraphTransformer implements TestRunTransformer {

    private static final String TEST_RUN_TO_GRAPH_XSL = "xsl/testrun-to-graph.xsl";

    private static final String GRAPH_JSON = "graph.json";

    @Override
    public void transform(String testPackXml, File outputDirectory) {
        String allureGraphBody = applyTransformation(testPackXml, TEST_RUN_TO_GRAPH_XSL);

        AllureGraph allureGraph = JAXB.unmarshal(
                new StringReader(allureGraphBody),
                AllureGraph.class
        );

        serialize(outputDirectory, GRAPH_JSON, allureGraph);
    }
}
