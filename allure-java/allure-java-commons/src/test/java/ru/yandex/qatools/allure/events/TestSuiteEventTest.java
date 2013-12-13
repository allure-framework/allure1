package ru.yandex.qatools.allure.events;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.qatools.allure.model.TestSuiteResult;

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
        new TestSuiteStartedEvent().withName("name").withUid("someuid").process(testSuite);
        assertThat(testSuite.getName(), is("name"));
        assertNotNull(testSuite.getStart());
        //TODO check parameters from annotations??
    }

    @Test
    public void testSuiteFinishedEvent() throws Exception {
        new TestSuiteFinishedEvent().withUid("someuid").process(testSuite);
        assertNotNull(testSuite.getStop());
    }
}
