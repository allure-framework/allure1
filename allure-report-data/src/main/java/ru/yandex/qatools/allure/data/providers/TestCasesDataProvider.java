package ru.yandex.qatools.allure.data.providers;

import ru.yandex.qatools.allure.data.AllureTestCase;
import ru.yandex.qatools.allure.data.AllureTestCasePack;
import ru.yandex.qatools.allure.data.utils.AllureReportUtils;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.Reader;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.12.13
 */
public class TestCasesDataProvider extends AbstractDataProvider {

    private static final String TESTRUN_TO_TESTCASE_PACK_XSL = "xsl/testrun-to-testcase-pack.xsl";

    private static final String TESTCASE_JSON_SUFFIX = "-testcase.json";

    @Override
    protected <T> long serialize(File outputDirectory, Class<T> type, String name, Reader reader) {
        AllureTestCasePack result = (AllureTestCasePack) JAXB.unmarshal(
                reader,
                type
        );

        long count = 0;
        for (AllureTestCase testCase : result.getTestCases()) {
            count += AllureReportUtils.serialize(
                    outputDirectory,
                    testCase.getUid() + TESTCASE_JSON_SUFFIX,
                    testCase
            );
        }

        return count;
    }

    @Override
    public String[] getXslTransformations() {
        return new String[]{TESTRUN_TO_TESTCASE_PACK_XSL};
    }

    @Override
    public String getJsonFileName() {
        return TESTCASE_JSON_SUFFIX;
    }

    @Override
    public Class<?> getType() {
        return AllureTestCasePack.class;
    }
}
