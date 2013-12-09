package ru.yandex.qatools.allure.junit.inject.testdata;

import org.junit.ClassRule;
import org.junit.Test;
import ru.yandex.qatools.allure.junit.TestSuiteReportRule;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 20.09.13
 */
public class TestClassWithTestSuiteRule {
    @ClassRule
    public static TestSuiteReportRule reportRule;

    @Test
    public void testName() throws Exception {

    }
}
