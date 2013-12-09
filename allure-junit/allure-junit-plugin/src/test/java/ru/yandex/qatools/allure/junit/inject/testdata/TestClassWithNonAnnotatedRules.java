package ru.yandex.qatools.allure.junit.inject.testdata;

import org.junit.Test;
import ru.yandex.qatools.allure.junit.TestCaseReportRule;
import ru.yandex.qatools.allure.junit.TestSuiteReportRule;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 09.12.13
 */
public class TestClassWithNonAnnotatedRules {

    public TestCaseReportRule testCaseReportRule;

    public static TestSuiteReportRule testSuiteReportRule;

    @Test
    public void testName() throws Exception {
    }
}
