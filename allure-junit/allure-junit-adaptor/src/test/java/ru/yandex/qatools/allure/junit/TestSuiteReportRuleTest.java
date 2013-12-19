package ru.yandex.qatools.allure.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.TestSuiteFinishedEvent;
import ru.yandex.qatools.allure.events.TestSuiteStartedEvent;

import java.lang.annotation.Annotation;
import java.util.Collections;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 19.12.13
 */
public class TestSuiteReportRuleTest {

    private TestSuiteReportRule testSuiteReportRule;

    private Allure allure;

    @Before
    public void setUp() throws Exception {
        allure = mock(Allure.class);

        testSuiteReportRule = new TestSuiteReportRule();
        testSuiteReportRule.setLifecycle(allure);
    }

    @Test
    public void testSuiteReportRuleStartedTest() throws Exception {
        Description description = mock(Description.class);
        when(description.getClassName()).thenReturn("some.name");
        when(description.getAnnotations()).thenReturn(Collections.<Annotation>emptyList());

        testSuiteReportRule.starting(description);
        String uid = testSuiteReportRule.getUid();
        verify(allure).fire(eq(new TestSuiteStartedEvent(uid, "some.name")));
    }

    @Test
    public void testSuiteReportRuleFinishedTest() throws Exception {
        testSuiteReportRule.finished(mock(Description.class));
        String uid = testSuiteReportRule.getUid();
        verify(allure).fire(eq(new TestSuiteFinishedEvent(uid)));
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(allure);
    }
}
