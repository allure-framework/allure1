package ru.yandex.qatools.allure;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.events.ClearStepStorageEvent;
import ru.yandex.qatools.allure.events.ClearTestStorageEvent;
import ru.yandex.qatools.allure.events.StepEvent;
import ru.yandex.qatools.allure.events.StepFinishedEvent;
import ru.yandex.qatools.allure.events.StepStartedEvent;
import ru.yandex.qatools.allure.events.TestCaseEvent;
import ru.yandex.qatools.allure.events.TestCaseFinishedEvent;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;
import ru.yandex.qatools.allure.events.TestSuiteEvent;
import ru.yandex.qatools.allure.events.TestSuiteFinishedEvent;
import ru.yandex.qatools.allure.events.TestSuiteStartedEvent;
import ru.yandex.qatools.allure.experimental.testdata.SimpleListener;
import ru.yandex.qatools.allure.model.Step;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.allure.utils.AllureResultsUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 04.06.14
 */
public class ListenersNotifierTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Allure allure;

    private final SimpleListener listener = new SimpleListener();

    @Before
    public void setUp() throws Exception {
        allure = new Allure();
        allure.addListener(listener);
        AllureResultsUtils.setResultsDirectory(folder.newFolder());
    }

    @Test
    public void stepStartedEventTest() throws Exception {
        allure.fire(new StepStartedEvent(""));
        assertThat(listener.get(SimpleListener.EventType.STEP_STARTED_EVENT), is(1));
    }

    @Test
    public void stepEventTest() throws Exception {
        allure.fire(new StepEvent() {
            @Override
            public void process(Step context) {
            }
        });
        assertThat(listener.get(SimpleListener.EventType.STEP_EVENT), is(1));
    }

    @Test
    public void stepFinishedEventTest() throws Exception {
        allure.fire(new StepFinishedEvent());
        assertThat(listener.get(SimpleListener.EventType.STEP_FINISHED_EVENT), is(1));
    }

    //

    @Test
    public void testcaseStartedEventTest() throws Exception {
        allure.fire(new TestCaseStartedEvent("", ""));
        assertThat(listener.get(SimpleListener.EventType.TESTCASE_STARTED_EVENT), is(1));
    }

    @Test
    public void testcaseEventTest() throws Exception {
        allure.fire(new TestCaseEvent() {
            @Override
            public void process(TestCaseResult context) {
            }
        });
        assertThat(listener.get(SimpleListener.EventType.TESTCASE_EVENT), is(1));
    }

    @Test
    public void testcaseFinishedEventTest() throws Exception {
        allure.fire(new TestCaseFinishedEvent());
        assertThat(listener.get(SimpleListener.EventType.TESTCASE_FINISHED_EVENT), is(1));
    }


    //

    @Test
    public void testsuiteEventTest() throws Exception {
        allure.fire(new TestSuiteEvent() {
            @Override
            public String getUid() {
                return "";
            }

            @Override
            public void process(TestSuiteResult context) {

            }
        });
        assertThat(listener.get(SimpleListener.EventType.TESTSUITE_EVENT), is(1));
    }

    @Test
    public void testsuiteFinishedEventTest() throws Exception {
        allure.fire(new TestSuiteStartedEvent("uid", "name"));
        allure.fire(new TestSuiteFinishedEvent("uid"));
        assertThat(listener.get(SimpleListener.EventType.TESTSUITE_FINISHED_EVENT), is(1));
    }

    @Test
    public void clearStepContextTest() throws Exception {
        allure.fire(new ClearStepStorageEvent());
        assertThat(listener.get(SimpleListener.EventType.CLEAR_STEP_STORAGE_EVENT), is(1));
    }

    @Test
    public void clearTestContextTest() throws Exception {
        allure.fire(new ClearTestStorageEvent());
        assertThat(listener.get(SimpleListener.EventType.CLEAR_TEST_STORAGE_EVENT), is(1));
    }
}