package ru.yandex.qatools.allure.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.*;

import java.lang.annotation.Annotation;
import java.util.Collections;

import static org.mockito.Mockito.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 19.12.13
 */
public class TestCaseReportRuleTest {

    private TestSuiteReportRule testSuiteReportRule;

    private TestCaseReportRule testCaseReportRule;

    private Allure allure;

    @Before
    public void setUp() throws Exception {
        testSuiteReportRule = mock(TestSuiteReportRule.class);
        when(testSuiteReportRule.getUid()).thenReturn("some.uid");
        testCaseReportRule = new TestCaseReportRule(testSuiteReportRule);

        allure = mock(Allure.class);
        testCaseReportRule.setLifecycle(allure);
    }

    @Test
    public void testCaseReportRuleStartedTest() throws Exception {
        Description description = mock(Description.class);
        when(description.getAnnotations()).thenReturn(Collections.<Annotation>emptyList());
        when(description.getMethodName()).thenReturn("some.method.name");

        testCaseReportRule.starting(description);
        verify(allure).fire(eq(new TestCaseStartedEvent("some.uid", "some.method.name")));
    }

    @Test
    public void testCaseReportRuleFailedTest() throws Exception {
        Throwable throwable = new Exception("atata");
        testCaseReportRule.failed(throwable, mock(Description.class));
        verify(allure).fire(eq(new TestCaseFailureEvent().withThrowable(throwable)));
    }

    @Test
    public void testCaseReportRuleSkippedTest() throws Exception {
        AssumptionViolatedException throwable = new AssumptionViolatedException("atata");
        testCaseReportRule.skipped(throwable, mock(Description.class));
        verify(allure).fire(eq(new TestCaseSkippedEvent().withThrowable(throwable)));
    }

    @Test
    public void testCaseReportRuleFinishedTest() throws Exception {
        testCaseReportRule.finished(mock(Description.class));
        verify(allure).fire(eq(new TestCaseFinishedEvent()));
    }

    @After
    public void tearDown() throws Exception {
        verify(testSuiteReportRule).getUid();
        verifyNoMoreInteractions(testSuiteReportRule);
        verifyNoMoreInteractions(allure);
    }


}
