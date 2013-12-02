package ru.yandex.qatools.allure.data.generators;

import ru.yandex.qatools.allure.data.*;
import ru.yandex.qatools.allure.data.json.GraphData;
import ru.yandex.qatools.allure.data.json.TestCasesData;
import ru.yandex.qatools.allure.data.json.XUnitData;
import ru.yandex.qatools.allure.data.utils.AllureReportUtils;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.flatten;
import static ch.lambdaj.Lambda.on;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 02.12.13
 */
public class TestRun {

    private static final String CREATE_ALLURE_XUNIT_XSL = "xsl/create-allure-xunit.xsl";

    private String allureTestRun;

    public TestRun(String allureTestRun) {
        this.allureTestRun = allureTestRun;
    }

    public XUnitData generateXUnitData() {
        return new XUnitData(generateAllureXUnit());
    }

    private AllureXUnit generateAllureXUnit() {
        return JAXB.unmarshal(new StringReader(generateAllureXUnitBody()), AllureXUnit.class);
    }

    private String generateAllureXUnitBody() {
        return AllureReportUtils.applyXslTransformation(
            CREATE_ALLURE_XUNIT_XSL,
            allureTestRun
        );
    }

    public TestCasesData generateTestCasesData() {
        return new TestCasesData(generateAllureTestCases());
    }

    private List<AllureTestCase> generateAllureTestCases() {
        return getAllureTestRun().getTestCases();
    }

    public GraphData generateGraphData() {
        return new GraphData(getAllureGraph());
    }

    private AllureGraph getAllureGraph() {
        AllureGraph allureGraph = new AllureGraph();
        List<AllureTestCaseInfo> testCases = flatten(extract(
                getAllureTestRun().getTestSuites(),
                on(AllureTestSuite.class).getTestCases()
        ));

        allureGraph.getTestCases().addAll(testCases);
        return allureGraph;
    }

    public AllureTestRun getAllureTestRun() {
        return JAXB.unmarshal(new StringReader(allureTestRun), AllureTestRun.class);
    }
}
