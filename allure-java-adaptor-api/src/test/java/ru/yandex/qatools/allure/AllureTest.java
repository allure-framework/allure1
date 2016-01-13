package ru.yandex.qatools.allure;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.events.MakeAttachmentEvent;
import ru.yandex.qatools.allure.events.RemoveAttachmentsEvent;
import ru.yandex.qatools.allure.events.TestCaseFinishedEvent;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;
import ru.yandex.qatools.allure.events.TestSuiteFinishedEvent;
import ru.yandex.qatools.allure.events.TestSuiteStartedEvent;
import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.Step;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.allure.utils.testdata.TestData;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static ru.yandex.qatools.allure.AllureUtils.listTestSuiteXmlFiles;
import static ru.yandex.qatools.allure.AllureUtils.unmarshalTestSuite;
import static ru.yandex.qatools.allure.AllureUtils.validateResults;
import static ru.yandex.qatools.allure.utils.testdata.TestData.randomName;
import static ru.yandex.qatools.allure.utils.testdata.TestData.randomTitle;
import static ru.yandex.qatools.allure.utils.testdata.TestData.randomUid;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.01.16
 */
public class AllureTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public Path resultsDirectory;

    public Allure allure;

    @Before
    public void setUp() throws Exception {
        resultsDirectory = folder.newFolder().toPath();
        allure = new Allure(new DummyConfig(resultsDirectory));
    }

    @Test
    public void shouldCreateTestSuite() throws Exception {
        String suiteUid = randomUid();
        String suiteName = randomName();

        allure.fire(new TestSuiteStartedEvent(suiteUid, suiteName));
        allure.fire(new TestSuiteFinishedEvent(suiteUid));

        validateResults(resultsDirectory);
        TestSuiteResult result = shouldBeOnlyOneSuite();
        assertThat(result.getName(), is(suiteName));
    }

    @Test
    public void shouldCreateTestCaseWithSuite() throws Exception {
        String suiteUid = randomUid();
        String suiteName = randomName();

        allure.fire(new TestSuiteStartedEvent(suiteUid, suiteName));

        String name = randomName();
        allure.fire(new TestCaseStartedEvent(suiteUid, name));
        allure.fire(new TestCaseFinishedEvent());

        allure.fire(new TestSuiteFinishedEvent(suiteUid));

        validateResults(resultsDirectory);
        TestSuiteResult result = shouldBeOnlyOneSuite();

        assertThat(result.getName(), is(suiteName));
        assertThat(result.getTestCases(), hasSize(1));
        assertThat(result.getTestCases().iterator().next().getName(), is(name));
    }

    @Test
    public void shouldCreateTestCaseWithoutSuiteStartedAsWell() throws Exception {
        String suiteUid = randomUid();

        String name = randomName();
        allure.fire(new TestCaseStartedEvent(suiteUid, name));
        allure.fire(new TestCaseFinishedEvent());

        allure.fire(new TestSuiteFinishedEvent(suiteUid));

        TestSuiteResult result = shouldBeOnlyOneSuite();

        assertThat(result.getName(), nullValue());
        assertThat(result.getTestCases(), hasSize(1));
        assertThat(result.getTestCases().iterator().next().getName(), is(name));
    }

    @Test
    public void shouldDeleteAttachments() throws Exception {
        byte[] body = TestData.randomAttachmentBody();
        String first = randomTitle();
        String second = randomTitle();
        allure.fire(new MakeAttachmentEvent(body, first, "text/plain"));
        allure.fire(new MakeAttachmentEvent(body, second, "application/json"));

        Step before = allure.getStepStorage().getLast();
        assertThat(before, notNullValue());
        assertThat(before.getAttachments(), hasSize(2));
        Iterator<Attachment> iterator = before.getAttachments().iterator();
        assertThat(iterator.next().getTitle(), is(first));
        assertThat(iterator.next().getTitle(), is(second));

        allure.fire(new RemoveAttachmentsEvent(".*txt"));

        Step after = allure.getStepStorage().getLast();
        assertThat(after, notNullValue());
        assertThat(after.getAttachments(), hasSize(1));
        assertThat(after.getAttachments().iterator().next().getTitle(), is(second));
    }

    @Test
    public void shouldDeleteAttachmentsFromPassedTests() throws Exception {

        allure = new Allure(new DummyConfig(resultsDirectory) {
            @Override
            public String getRemoveAttachmentsPattern() {
                return ".*txt";
            }
        });

        byte[] body = TestData.randomAttachmentBody();
        String first = randomTitle();
        String second = randomTitle();

        String suiteUid = randomUid();

        String name = randomName();
        allure.fire(new TestCaseStartedEvent(suiteUid, name));

        allure.fire(new MakeAttachmentEvent(body, first, "text/plain"));
        allure.fire(new MakeAttachmentEvent(body, second, "application/json"));

        Step before = allure.getStepStorage().getLast();
        assertThat(before, notNullValue());
        assertThat(before.getAttachments(), hasSize(2));
        Iterator<Attachment> iterator = before.getAttachments().iterator();
        assertThat(iterator.next().getTitle(), is(first));
        assertThat(iterator.next().getTitle(), is(second));

        allure.fire(new TestCaseFinishedEvent());

        TestCaseResult result = allure.getTestSuiteStorage().get(suiteUid).getTestCases().iterator().next();

        assertThat(result, notNullValue());
        assertThat(result.getAttachments(), hasSize(1));
        assertThat(result.getAttachments().iterator().next().getTitle(), is(second));
    }

    private TestSuiteResult shouldBeOnlyOneSuite() throws IOException {
        List<Path> suites = listTestSuiteXmlFiles(resultsDirectory);
        assertThat(suites, hasSize(1));

        return unmarshalTestSuite(suites.iterator().next());
    }
}