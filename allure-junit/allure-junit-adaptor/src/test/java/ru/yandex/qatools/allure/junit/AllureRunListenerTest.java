package ru.yandex.qatools.allure.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.*;
import ru.yandex.qatools.allure.junit.testdata.TestData;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 16.01.14
 */
public class AllureRunListenerTest {

    private AllureRunListener runListener;

    private Allure allure;

    @Before
    public void setUp() throws Exception {
        runListener = spy(new AllureRunListener());

        allure = mock(Allure.class);
        runListener.setLifecycle(allure);
    }

    @Test
    public void getLifecycleTest() throws Exception {
        assertThat(runListener.getLifecycle(), is(allure));
    }

    @Test
    public void testSuiteStartedTest() throws Exception {
        runListener.testSuiteStarted("some-uid", "some-name", Collections.<Annotation>emptyList());
        verify(allure).fire(eq(new TestSuiteStartedEvent("some-uid", "some-name")));
    }

    @Test
    public void testSuiteFinishedTest() throws Exception {
        runListener.testSuiteFinished("some-uid");
        verify(allure).fire(eq(new TestSuiteFinishedEvent("some-uid")));
    }

    @Test
    public void testStartedTest() throws Exception {
        Description description = mock(Description.class);
        when(description.getAnnotations()).thenReturn(Collections.<Annotation>emptyList());
        when(description.getMethodName()).thenReturn("some.method.name");

        doReturn("some.uid").when(runListener).getSuiteUid(description);
        runListener.testStarted(description);

        verify(allure).fire(eq(new TestCaseStartedEvent("some.uid", "some.method.name")));
    }

    @Test
    public void testFinishedTest() throws Exception {
        Description description = mock(Description.class);
        runListener.testFinished(description);
        verify(allure).fire(eq(new TestCaseFinishedEvent()));
    }

    //if test class annotated with @Ignored, do nothing
    @Test
    public void testSuiteIgnoredTest() throws Exception {
        Description description = mock(Description.class);
        runListener.testIgnored(description);
    }

    @Test
    public void testIgnoredTest() throws Exception {
        Description description = mock(Description.class);
        when(description.getMethodName()).thenReturn("some.method.name");

        doNothing().when(runListener).testStarted(description);
        doNothing().when(runListener).testFinished(description);
        runListener.testIgnored(description);

        verify(allure).fire(any(TestCaseSkippedEvent.class));
    }

    @Test
    public void testAssumptionFailureTest() throws Exception {
        Failure failure = mock(Failure.class);
        Throwable exception = mock(Throwable.class);
        when(failure.getException()).thenReturn(exception);

        runListener.testAssumptionFailure(failure);

        verify(allure).fire(eq(new TestCaseSkippedEvent().withThrowable(exception)));
    }

    @Test
    public void testFailureTest() throws Exception {
        Failure failure = mock(Failure.class);
        Throwable exception = mock(Throwable.class);
        when(failure.getException()).thenReturn(exception);

        runListener.testFailure(failure);

        verify(allure).fire(eq(new TestCaseFailureEvent().withThrowable(exception)));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testRunFinishedTest() throws Exception {
        Result result = mock(Result.class);
        Map<String, String> fakeSuites = (Map<String, String>) mock(Map.class);

        doReturn(Arrays.asList("first.uid", "second.uid")).when(fakeSuites).values();

        doReturn(fakeSuites).when(runListener).getSuites();

        runListener.testRunFinished(result);

        verify(allure).fire(eq(new TestSuiteFinishedEvent("first.uid")));
        verify(allure).fire(eq(new TestSuiteFinishedEvent("second.uid")));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getSuiteUidIfNeedToGenerateUidTest() throws Exception {
        Description description = mock(Description.class);
        when(description.getAnnotations()).thenReturn(Collections.<Annotation>emptyList());
        when(description.getClassName()).thenReturn("some.suite.name");
        when(description.getTestClass()).thenReturn((Class) TestData.class);

        Map<String, String> fakeSuites = (Map<String, String>) mock(Map.class);
        doReturn(false).when(fakeSuites).containsKey("some.suite.name");

        doReturn(fakeSuites).when(runListener).getSuites();
        runListener.getSuiteUid(description);

        verify(allure).fire(any(TestSuiteStartedEvent.class));
        verify(fakeSuites).put(eq("some.suite.name"), anyString());
        verify(fakeSuites).get("some.suite.name");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getSuiteUidTest() throws Exception {
        Description description = mock(Description.class);
        when(description.getClassName()).thenReturn("some.suite.name");

        Map<String, String> fakeSuites = (Map<String, String>) mock(Map.class);
        doReturn(true).when(fakeSuites).containsKey(anyString());

        doReturn(fakeSuites).when(runListener).getSuites();
        runListener.getSuiteUid(description);

        verify(fakeSuites).get("some.suite.name");
    }

    @After
    public void tearDown() throws Exception {
        verifyNoMoreInteractions(allure);
    }
}
