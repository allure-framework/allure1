package ru.yandex.qatools.allure.data.json;

import ru.yandex.qatools.allure.data.AllureTestCase;
import ru.yandex.qatools.allure.data.utils.AllureReportUtils;

import java.io.File;
import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 02.12.13
 */
public class TestCasesData implements JSONObject {

    private static final String FILE_NAME_SUFFIX = "-testcase.json";

    private List<AllureTestCase> testCases;

    public TestCasesData(List<AllureTestCase> testCases) {
        this.testCases = testCases;
    }

    @Override
    public void serialize(File outputDirectory) {
        for (AllureTestCase testCase : testCases) {
            AllureReportUtils.serialize(
                    outputDirectory,
                    testCase.getUid() + FILE_NAME_SUFFIX,
                    testCase
            );
        }
    }
}
