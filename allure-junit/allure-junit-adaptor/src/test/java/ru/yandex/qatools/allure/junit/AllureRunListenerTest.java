package ru.yandex.qatools.allure.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.internal.AssumptionViolatedException;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.*;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

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

        inOrder(allure).verify(allure).fire(eq(new ClearStepStorageEvent()));
        inOrder(allure).verify(allure).fire(eq(new TestCaseStartedEvent("some.uid", "some.method.name")));
    }

    @Test
    public void testFinishedTest() throws Exception {
        Description description = mock(Description.class);
        runListener.testFinished(description);
        verify(allure).fire(eq(new TestCaseFinishedEvent()));
    }

    @Test
    public void testIgnoredTest() throws Exception {
        AssumptionViolatedException exception = mock(AssumptionViolatedException.class);
        doReturn(exception).when(runListener).getIgnoredException(any(Description.class));
        doNothing().when(runListener).createFakeTestCaseWithFailure(any(Description.class), eq(exception));
    }

    @Test
    public void testAssumptionFailureTest() throws Exception {
        Failure failure = mockFailure();
        doNothing().when(runListener).testFailure(eq(failure));
        runListener.testAssumptionFailure(failure);
    }

    @Test
    public void testFailureTest() throws Exception {
        Description description = mock(Description.class);
        when(description.isTest()).thenReturn(true);
        Throwable exception = mock(Throwable.class);

        Failure failure = mockFailureWith(exception, description);

        runListener.testFailure(failure);

        TestCaseFailureEvent event = new TestCaseFailureEvent();
        event.setThrowable(exception);

        verify(allure).fire(eq(event));
    }

    @Test
    public void testFailureWithAssumptionTest() throws Exception {
        Description description = mock(Description.class);
        when(description.isTest()).thenReturn(true);
        Throwable exception = mock(AssumptionViolatedException.class);

        Failure failure = mockFailureWith(exception, description);

        runListener.testFailure(failure);

        TestCaseSkippedEvent event = new TestCaseSkippedEvent();
        event.setThrowable(exception);

        verify(allure).fire(eq(event));
    }

    @Test
    public void suiteFailureTest() throws Exception {
        Description description = mock(Description.class);
        when(description.isTest()).thenReturn(false);
        Throwable exception = mock(AssumptionViolatedException.class);

        Failure failure = mockFailureWith(exception, description);
        doNothing().when(runListener).createFakeTestCaseWithFailure(eq(description), eq(exception));

        runListener.testFailure(failure);
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

    public static Failure mockFailureWith(Throwable throwable, Description description) {
        Failure failure = mock(Failure.class);
        when(failure.getException()).thenReturn(throwable);
        when(failure.getDescription()).thenReturn(description);
        return failure;
    }

    public static Failure mockFailure() {
        return mockFailureWith(mock(Throwable.class), mock(Description.class));
    }

}
