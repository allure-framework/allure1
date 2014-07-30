package ru.yandex.qatools.allure.events;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.qatools.allure.model.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.*;


/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.12.13
 */
public class TestCaseEventTest {

    private TestCaseResult testCase;

    @Before
    public void setUp() throws Exception {
        testCase = spy(new TestCaseResult());
    }

    @Test
    public void testCaseStartedEvent() throws Exception {
        new TestCaseStartedEvent("suite.uid", "name").process(testCase);
        verify(testCase).setName("name");
        verify(testCase).setTitle(null);
        verify(testCase).setDescription(null);
        verify(testCase).setLabels(Collections.<Label>emptyList());
        verify(testCase).setStart(anyLong());
        verify(testCase).setStatus(Status.PASSED);
        verifyNoMoreInteractions(testCase);
    }

    @Test
    public void testCaseStartedEventTitle() throws Exception {
        new TestCaseStartedEvent("suite.uid", "name").withTitle("some.title").process(testCase);
        verify(testCase).setName("name");
        verify(testCase).setTitle("some.title");
        verify(testCase).setDescription(null);
        verify(testCase).setLabels(Collections.<Label>emptyList());
        verify(testCase).setStart(anyLong());
        verify(testCase).setStatus(Status.PASSED);
        verifyNoMoreInteractions(testCase);
    }

    @Test
    public void testCaseStartedEventDescription() throws Exception {
        Description description = new Description()
                .withValue("some.description")
                .withType(DescriptionType.MARKDOWN);

        new TestCaseStartedEvent("suite.uid", "name").withDescription(description).process(testCase);
        verify(testCase).setName("name");
        verify(testCase).setTitle(null);
        verify(testCase).setDescription(description);
        verify(testCase).setLabels(Collections.<Label>emptyList());
        verify(testCase).setStart(anyLong());
        verify(testCase).setStatus(Status.PASSED);
        verifyNoMoreInteractions(testCase);
    }

    @Test
    public void testCaseStartedEventLabels() throws Exception {
        Label label = new Label().withName("label.name").withValue("label.value");
        new TestCaseStartedEvent("suite.uid", "name").withLabels(label).process(testCase);
        verify(testCase).setName("name");
        verify(testCase).setTitle(null);
        verify(testCase).setDescription(null);
        verify(testCase).setLabels(Arrays.asList(label));
        verify(testCase).setStart(anyLong());
        verify(testCase).setStatus(Status.PASSED);
        verifyNoMoreInteractions(testCase);
    }

    @Test
    public void testCaseFinishedEvent() throws Exception {
        new TestCaseFinishedEvent().process(testCase);
        verify(testCase).setStop(anyLong());
        verifyNoMoreInteractions(testCase);
    }

    @Test
    public void testCaseCanceledEvent() throws Exception {
        Throwable throwable = new Exception("atata");
        new TestCaseCanceledEvent().withThrowable(throwable).process(testCase);
        verify(testCase).setFailure(any(Failure.class));
        verify(testCase).setStatus(Status.CANCELED);
        verifyNoMoreInteractions(testCase);
    }

    @Test
    public void testCaseCanceledEventWithNullFailure() throws Exception {
        new TestCaseCanceledEvent().withThrowable(null).process(testCase);
        verify(testCase).setFailure(any(Failure.class));
        verify(testCase).setStatus(Status.CANCELED);
        verifyNoMoreInteractions(testCase);
    }

    @Test
    public void testCaseFailureEventFailed() throws Exception {
        Throwable throwable = new AssertionError("atata");
        new TestCaseFailureEvent().withThrowable(throwable).process(testCase);
        verify(testCase).setFailure(any(Failure.class));
        verify(testCase).setStatus(Status.FAILED);
        verifyNoMoreInteractions(testCase);
    }

    @Test
    public void testCaseFailureEventBroken() throws Exception {
        Throwable throwable = new Exception("atata");
        new TestCaseFailureEvent().withThrowable(throwable).process(testCase);
        verify(testCase).setFailure(any(Failure.class));
        verify(testCase).setStatus(Status.BROKEN);
        verifyNoMoreInteractions(testCase);
    }

    @Test
    public void testCaseFailureEventBrokenWithNullFailure() throws Exception {
        new TestCaseFailureEvent().withThrowable(null).process(testCase);
        verify(testCase).setFailure(any(Failure.class));
        verify(testCase).setStatus(Status.BROKEN);
        verifyNoMoreInteractions(testCase);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCaseAddParameterTest() throws Exception {
        List parameters = mock(List.class);
        doReturn(parameters).when(testCase).getParameters();
        new AddParameterEvent("some-name", "some-value").process(testCase);
        verify(testCase).getParameters();
        verify(parameters).add(any(Parameter.class));
        verifyNoMoreInteractions(parameters);
        verifyNoMoreInteractions(testCase);
    }

    @Test
    public void testCasePendingEventTest() throws Exception {
        Throwable throwable = new Exception("atata");
        new TestCasePendingEvent().withThrowable(throwable).process(testCase);
        verify(testCase).setStatus(Status.PENDING);
        verify(testCase).setFailure(any(Failure.class));
        verifyNoMoreInteractions(testCase);
    }
}
