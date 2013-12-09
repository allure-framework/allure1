package ru.yandex.qatools.allure.data.providers;

import ru.yandex.qatools.allure.data.AllureTestCase;
import ru.yandex.qatools.allure.data.AllureTestCasePack;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.StringReader;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.serialize;
import static ru.yandex.qatools.allure.data.utils.XslTransformationUtil.applyTransformation;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.12.13
 */
public class TestCasesDataProvider implements DataProvider {

    private static final String TESTRUN_TO_TESTCASE_PACK_XSL = "xsl/testrun-to-testcase-pack.xsl";

    private static final String TESTCASE_JSON_SUFFIX = "-testcase.json";

    @Override
    public void provide(String testPack, File outputDirectory) {
        String allureTestCasePackBody = applyTransformation(testPack, TESTRUN_TO_TESTCASE_PACK_XSL);

        AllureTestCasePack allureTestCasePack = JAXB.unmarshal(
                new StringReader(allureTestCasePackBody),
                AllureTestCasePack.class
        );

        for (AllureTestCase testCase : allureTestCasePack.getTestCases()) {
            serialize(
                    outputDirectory,
                    testCase.getUid() + TESTCASE_JSON_SUFFIX,
                    testCase
            );
        }
    }
}
