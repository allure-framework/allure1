package ru.yandex.qatools.allure.events;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.12.13
 */
public class TestSuiteEventTest {
    private TestSuiteResult testSuite;

    @Before
    public void setUp() throws Exception {
        testSuite = new TestSuiteResult();
    }

    @Test
    public void testSuiteStartedEvent() throws Exception {
        new TestSuiteStartedEvent("some.uid", "name").process(testSuite);
        assertThat(testSuite.getName(), is("name"));
        assertNotNull(testSuite.getStart());
    }

    @Test
    public void testSuiteStartedEventTitle() throws Exception {
        new TestSuiteStartedEvent("suite.uid", "name").withTitle("some.title").process(testSuite);
        assertThat(testSuite.getTitle(), is("some.title"));
    }

    @Test
    public void testSuiteStartedEventDescription() throws Exception {
        new TestSuiteStartedEvent("suite.uid", "name").withDescription("some.description").process(testSuite);
        assertThat(testSuite.getDescription(), is("some.description"));
    }

    @Test
    public void testSuiteStartedEventBehavior() throws Exception {
        Label label = new Label().withName("label.name").withValue("label.value");
        new TestSuiteStartedEvent("suite.uid", "name").withLabels(label).process(testSuite);
        assertThat(testSuite.getLabels(), hasSize(1));
        assertThat(testSuite.getLabels().get(0), having(on(Label.class).getName(), equalTo("label.name")));
        assertThat(testSuite.getLabels().get(0), having(on(Label.class).getValue(), equalTo("label.value")));
    }
    
    @Test
    public void testSuiteFinishedEvent() throws Exception {
        new TestSuiteFinishedEvent("some.uid").process(testSuite);
        assertNotNull(testSuite.getStop());
    }
}
