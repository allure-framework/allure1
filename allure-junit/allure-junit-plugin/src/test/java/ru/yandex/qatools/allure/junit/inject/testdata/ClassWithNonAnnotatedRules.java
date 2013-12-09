package ru.yandex.qatools.allure.junit.inject.testdata;

import ru.yandex.qatools.allure.junit.TestCaseReportRule;
import ru.yandex.qatools.allure.junit.TestSuiteReportRule;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 09.12.13
 */
public class ClassWithNonAnnotatedRules {

    public TestCaseReportRule testCaseReportRule;

    public static TestSuiteReportRule testSuiteReportRule;
}
