package ru.yandex.qatools.allure.events;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.Step;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.12.13
 */
public class StepEventTest {

    private Step step;

    @Before
    public void setUp() throws Exception {
        step = new Step();
    }

    @Test
    public void testStepFailureEventFailed() throws Exception {
        new StepFailureEvent().withThrowable(new AssertionError()).process(step);
        assertThat(step.getStatus(), is(Status.FAILED));
    }

    @Test
    public void testStepFailureEventBroken() throws Exception {
        new StepFailureEvent().withThrowable(new Exception()).process(step);
        assertThat(step.getStatus(), is(Status.BROKEN));
    }

    @Test
    public void testStepStartedEvent() throws Exception {
        new StepStartedEvent().withName("name").process(step);
        assertThat(step.getName(), is("name"));
        assertNotNull(step.getStart());
        assertThat(step.getStatus(), is(Status.PASSED));
    }

    @Test
    public void testStepFinishedEvent() throws Exception {
        new StepFinishedEvent().process(step);
        assertNotNull(step.getStop());
    }

    @Test
    public void testStepSkippedEvent() throws Exception {
        new StepSkippedEvent().process(step);
        assertThat(step.getStatus(), is(Status.SKIPPED));
    }
}
