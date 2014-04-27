package ru.yandex.qatools.allure.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.*;
import ru.yandex.qatools.allure.junit.testdata.AssertionErrorInBeforeClass;
import ru.yandex.qatools.allure.junit.testdata.ExceptionInBeforeClass;
import ru.yandex.qatools.allure.junit.testdata.WithOneFailedCaseOnly;
import ru.yandex.qatools.allure.junit.testdata.TestData;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.runner.Description.createSuiteDescription;
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
        when(failure.getDescription()).thenReturn(createSuiteDescription(this.getClass()));

        runListener.testFailure(failure);

        verify(allure).fire(eq(new TestCaseFailureEvent().withThrowable(exception)));
    }

    @Test
    public void runningTestWithExceptionInBeforeDoNotCallTestStartedEvent() throws Exception {
        //given
        Class<ExceptionInBeforeClass> testclassWithBCException = ExceptionInBeforeClass.class;
        JUnitCore core = new JUnitCore();
        core.addListener(runListener);

        //when
        core.run(testclassWithBCException);

        //then
        verify(allure, never()).fire(any(TestCaseEvent.class));
        verify(runListener).testRunStarted(notNull(Description.class));
        verify(runListener, times(2)).getSuiteUid(any(Description.class));

        String uid = runListener.getSuites().get(testclassWithBCException.getCanonicalName());

        ArgumentCaptor<Failure> fail = ArgumentCaptor.forClass(Failure.class);
        verify(runListener).testFailure(fail.capture());

        InOrder inOrder = inOrder(allure);
        inOrder.verify(allure).fire(new TestSuiteStartedEvent(uid, testclassWithBCException.getCanonicalName()));
        inOrder.verify(allure).fire(new TestSuiteFailureEvent(uid).withThrowable(fail.getValue().getException()));
        inOrder.verify(allure).fire(new TestSuiteFinishedEvent(uid));
    }

    @Test
    public void ifJUnitProviderDoNotCallRunStartedWeLogFailOnFailure() throws Exception {
        //given
        Class<AssertionErrorInBeforeClass> testclassWithBCException = AssertionErrorInBeforeClass.class;
        JUnitCore core = new JUnitCore();
        core.addListener(runListener);

        doNothing().when(runListener).testRunStarted(any(Description.class));
        //when
        core.run(testclassWithBCException);

        //then

        // It don't call in testRunStarted, only on fail
        verify(runListener, times(1)).getSuiteUid(any(Description.class));
        verify(allure, never()).fire(any(TestCaseEvent.class));

        String uid = runListener.getSuites().get(testclassWithBCException.getCanonicalName());

        ArgumentCaptor<Failure> fail = ArgumentCaptor.forClass(Failure.class);
        verify(runListener).testFailure(fail.capture());

        InOrder inOrder = inOrder(allure);
        inOrder.verify(allure).fire(new TestSuiteStartedEvent(uid, testclassWithBCException.getCanonicalName()));
        inOrder.verify(allure).fire(new TestSuiteFailureEvent(uid).withThrowable(fail.getValue().getException()));
        inOrder.verify(allure).fire(new TestSuiteFinishedEvent(uid));
    }


    @Test
    public void failedCaseShouldNotFireSuiteFailEvent() throws Exception {
        //given
        Class<WithOneFailedCaseOnly> failedCaseClass = WithOneFailedCaseOnly.class;
        JUnitCore core = new JUnitCore();
        core.addListener(runListener);

        //when
        core.run(failedCaseClass);

        //then
        String uid = runListener.getSuites().get(failedCaseClass.getCanonicalName());
        ArgumentCaptor<Description> description = ArgumentCaptor.forClass(Description.class);
        ArgumentCaptor<Failure> fail = ArgumentCaptor.forClass(Failure.class);
        verify(runListener).testFailure(fail.capture());
        verify(runListener).testStarted(description.capture());

        InOrder inOrder = inOrder(allure);
        inOrder.verify(allure).fire(new TestSuiteStartedEvent(uid, failedCaseClass.getCanonicalName()));
        inOrder.verify(allure).fire(new TestCaseStartedEvent(uid, description.getValue().getMethodName()));
        inOrder.verify(allure).fire(new TestCaseFailureEvent().withThrowable(fail.getValue().getException()));
        inOrder.verify(allure).fire(new TestCaseFinishedEvent());
        inOrder.verify(allure).fire(new TestSuiteFinishedEvent(uid));
    }


    @Test
    public void testRunStartedShouldAddSuiteToSuitesMap() throws Exception {
        Description description = mock(Description.class);
        final Description descriptionChild = mock(Description.class);

        //given
        when(description.getChildren()).thenReturn(new ArrayList<Description>() {{
            add(descriptionChild);
        }});
        when(descriptionChild.getClassName()).thenReturn(this.getClass().getSimpleName());
        doReturn(this.getClass()).when(descriptionChild).getTestClass();

        //when
        runListener.testRunStarted(description);

        //then
        verify(runListener).testSuiteStarted(
                anyString(),
                eq(this.getClass().getSimpleName()),
                eq(asList(this.getClass().getAnnotations()))
        );
        verify(allure).fire(any(TestSuiteStartedEvent.class));
        assertThat("Suite should be added to map", runListener.getSuites(), hasKey(this.getClass().getSimpleName()));
    }


    @SuppressWarnings("unchecked")
    @Test
    public void testRunFinishedTest() throws Exception {
        Result result = mock(Result.class);
        Map<String, String> fakeSuites = (Map<String, String>) mock(Map.class);

        doReturn(asList("first.uid", "second.uid")).when(fakeSuites).values();

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
