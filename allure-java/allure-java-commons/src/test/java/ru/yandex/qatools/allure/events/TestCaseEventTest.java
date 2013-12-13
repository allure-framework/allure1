package ru.yandex.qatools.allure.events;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;

import static org.hamcrest.core.Is.is;
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
        new TestCaseStartedEvent().withName("name").withSuiteUid("suite.uid").process(testCase);
        assertThat(testCase.getName(), is("name"));
        assertThat(testCase.getStatus(), is(Status.PASSED));
        assertThat(testCase.getSeverity(), is(SeverityLevel.NORMAL));
        assertNotNull(testCase.getStart());
        //TODO check parameters from annotations??
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
