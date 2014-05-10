package ru.yandex.qatools.allure.events;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.qatools.allure.model.Description;
import ru.yandex.qatools.allure.model.DescriptionType;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.12.13
 */
public class TestSuiteEventTest {
    private TestSuiteResult testSuite;

    @Before
    public void setUp() throws Exception {
        testSuite = mock(TestSuiteResult.class);
    }

    @Test
    public void testSuiteStartedEvent() throws Exception {
        new TestSuiteStartedEvent("some.uid", "name").process(testSuite);
        verify(testSuite).setStart(anyLong());
        verify(testSuite).setName("name");
        verify(testSuite).setTitle(null);
        verify(testSuite).setDescription(null);
        verify(testSuite).setLabels(Collections.<Label>emptyList());
        verifyNoMoreInteractions(testSuite);
    }

    @Test
    public void testSuiteStartedEventTitle() throws Exception {
        new TestSuiteStartedEvent("suite.uid", "name").withTitle("some.title").process(testSuite);
        verify(testSuite).setStart(anyLong());
        verify(testSuite).setName("name");
        verify(testSuite).setTitle("some.title");
        verify(testSuite).setDescription(null);
        verify(testSuite).setLabels(Collections.<Label>emptyList());
        verifyNoMoreInteractions(testSuite);
    }

    @Test
    public void testSuiteStartedEventDescription() throws Exception {
        Description description = new Description()
                .withValue("some.description")
                .withType(DescriptionType.MARKDOWN);

        new TestSuiteStartedEvent("suite.uid", "name").withDescription(description).process(testSuite);
        verify(testSuite).setStart(anyLong());
        verify(testSuite).setName("name");
        verify(testSuite).setTitle(null);
        verify(testSuite).setDescription(description);
        verify(testSuite).setLabels(Collections.<Label>emptyList());
        verifyNoMoreInteractions(testSuite);
    }

    @Test
    public void testSuiteStartedEventBehavior() throws Exception {
        Label label = new Label().withName("label.name").withValue("label.value");
        new TestSuiteStartedEvent("suite.uid", "name").withLabels(label).process(testSuite);
        verify(testSuite).setStart(anyLong());
        verify(testSuite).setName("name");
        verify(testSuite).setTitle(null);
        verify(testSuite).setDescription(null);
        verify(testSuite).setLabels(Arrays.asList(label));
        verifyNoMoreInteractions(testSuite);
    }
    
    @Test
    public void testSuiteFinishedEvent() throws Exception {
        new TestSuiteFinishedEvent("some.uid").process(testSuite);
        verify(testSuite).setStop(anyLong());
        verifyNoMoreInteractions(testSuite);
    }
}
