package ru.yandex.qatools.allure.data.transform;

import ru.yandex.qatools.allure.data.AllureTestCase;
import ru.yandex.qatools.allure.data.AllureTestRun;
import ru.yandex.qatools.allure.data.utils.AllureReportUtils;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.StringReader;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.12.13
 */
public class TestCasesTransformer implements TestRunTransformer {

    private static final String TESTCASE_JSON_SUFFIX = "-testcase.json";

    @Override
    public void transform(String xml, File outputDirectory) {
        AllureTestRun allureTestRun =  JAXB.unmarshal(new StringReader(xml), AllureTestRun.class);

        for (AllureTestCase testCase : allureTestRun.getTestCases()) {
            AllureReportUtils.serialize(
                    outputDirectory,
                    testCase.getUid() + TESTCASE_JSON_SUFFIX,
                    testCase
            );
        }
    }
}
