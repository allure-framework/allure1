package ru.yandex.qatools.allure.junit.inject.testdata;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import ru.yandex.qatools.allure.junit.TestCaseReportRule;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 20.09.13
 */
public class TestClassWithTestCaseRule {
    @Rule
    public TestCaseReportRule reportRule;

    @Test
    @Ignore
    public void testName() throws Exception {

    }
}
