package ru.yandex.qatools.allure.events;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.qatools.allure.annotations.FeatureClass;
import ru.yandex.qatools.allure.annotations.StoryClass;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;


/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.12.13
 */
public class TestCaseEventTest {

    private TestCaseResult testCase;

    @Before
    public void setUp() throws Exception {
        testCase = new TestCaseResult();
    }

    @Test
    public void testCaseStartedEvent() throws Exception {
        new TestCaseStartedEvent("suite.uid", "name").process(testCase);
        assertThat(testCase.getName(), is("name"));
        assertThat(testCase.getStatus(), is(Status.PASSED));
        assertThat(testCase.getSeverity(), is(SeverityLevel.NORMAL));
        assertNotNull(testCase.getStart());
    }

    @Test
    public void testCaseStartedEventSeverity() throws Exception {
        new TestCaseStartedEvent("suite.uid", "name").withSeverity(SeverityLevel.BLOCKER).process(testCase);
        assertThat(testCase.getSeverity(), is(SeverityLevel.BLOCKER));
    }

    @Test
    public void testCaseStartedEventTitle() throws Exception {
        new TestCaseStartedEvent("suite.uid", "name").withTitle("some.title").process(testCase);
        assertThat(testCase.getTitle(), is("some.title"));
    }

    @Test
    public void testCaseStartedEventDescription() throws Exception {
        new TestCaseStartedEvent("suite.uid", "name").withDescription("some.description").process(testCase);
        assertThat(testCase.getDescription(), is("some.description"));
    }

    @Test
    public void testCaseStartedEventBehavior() throws Exception {
        Label label = new Label().withName("label.name").withValue("label.value");
        new TestCaseStartedEvent("suite.uid", "name").withLabels(label).process(testCase);
        assertThat(testCase.getLabels(), hasSize(1));
        assertThat(testCase.getLabels().get(0), having(on(Label.class).getName(), equalTo("label.name")));
        assertThat(testCase.getLabels().get(0), having(on(Label.class).getValue(), equalTo("label.value")));
    }

    @Test
    public void testCaseFinishedEvent() throws Exception {
        new TestCaseFinishedEvent().process(testCase);
        assertNotNull(testCase.getStop());
    }

    @Test
    public void testCaseSkippedEvent() throws Exception {
        new TestCaseSkippedEvent().withThrowable(new Exception("atata")).process(testCase);
        assertNotNull(testCase.getFailure());
        assertThat(testCase.getFailure().getMessage(), is("Exception: atata"));
        assertThat(testCase.getStatus(), is(Status.SKIPPED));
    }

    @Test
    public void testCaseFailureEventFailed() throws Exception {
        new TestCaseFailureEvent().withThrowable(new AssertionError("atata")).process(testCase);
        assertNotNull(testCase.getFailure());
        assertThat(testCase.getFailure().getMessage(), is("AssertionError: atata"));
        assertThat(testCase.getStatus(), is(Status.FAILED));
    }

    @Test
    public void testCaseFailureEventBroken() throws Exception {
        new TestCaseFailureEvent().withThrowable(new Exception("atata")).process(testCase);
        assertNotNull(testCase.getFailure());
        assertThat(testCase.getFailure().getMessage(), is("Exception: atata"));
        assertThat(testCase.getStatus(), is(Status.BROKEN));
    }
}
