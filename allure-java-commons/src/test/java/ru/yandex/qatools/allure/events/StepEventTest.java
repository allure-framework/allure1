package ru.yandex.qatools.allure.events;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.Step;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.12.13
 */
public class StepEventTest {

    private Step step;

    @Before
    public void setUp() throws Exception {
        step = mock(Step.class);
    }

    @Test
    public void testStepFailureEventFailed() throws Exception {
        new StepFailureEvent().withThrowable(new AssertionError()).process(step);
        verify(step).setStatus(Status.FAILED);
        verifyNoMoreInteractions(step);
    }

    @Test
    public void testStepFailureEventBroken() throws Exception {
        new StepFailureEvent().withThrowable(new Exception()).process(step);
        verify(step).setStatus(Status.BROKEN);
        verifyNoMoreInteractions(step);
    }

    @Test
    public void testStepStartedEvent() throws Exception {
        new StepStartedEvent("name").process(step);
        verify(step).setName("name");
        verify(step).setStart(anyLong());
        verify(step).setStatus(Status.PASSED);
        verify(step).setTitle(null);
        verifyNoMoreInteractions(step);
    }

    @Test
    public void testStepFinishedEvent() throws Exception {
        new StepFinishedEvent().process(step);
        verify(step).setStop(anyLong());
        verifyNoMoreInteractions(step);
    }

    @Test
    public void testStepSkippedEvent() throws Exception {
        new StepSkippedEvent().process(step);
        verify(step).setStatus(Status.SKIPPED);
        verifyNoMoreInteractions(step);
    }
}
