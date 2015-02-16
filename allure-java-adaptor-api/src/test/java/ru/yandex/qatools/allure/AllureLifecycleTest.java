package ru.yandex.qatools.allure;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.xml.sax.SAXException;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.events.ClearStepStorageEvent;
import ru.yandex.qatools.allure.events.ClearTestStorageEvent;
import ru.yandex.qatools.allure.events.MakeAttachmentEvent;
import ru.yandex.qatools.allure.events.StepFinishedEvent;
import ru.yandex.qatools.allure.events.StepStartedEvent;
import ru.yandex.qatools.allure.events.TestCaseFinishedEvent;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;
import ru.yandex.qatools.allure.events.TestSuiteFinishedEvent;
import ru.yandex.qatools.allure.events.TestSuiteStartedEvent;
import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.Step;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.allure.utils.AllureResultsUtils;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static ru.yandex.qatools.allure.commons.AllureFileUtils.listTestSuiteFiles;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.12.13
 */
public class AllureLifecycleTest {

    private static final String UTF_8 = "UTF-8";
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public File resultsDirectory;

    @Before
    public void setUp() throws Exception {
        resultsDirectory = folder.newFolder();
        AllureResultsUtils.setResultsDirectory(resultsDirectory);
    }

    @Test
    public void allureLifecycleTest() throws Exception {
        String uid = UUID.randomUUID().toString();
        TestSuiteResult testSuite = fireTestSuiteStart(uid);
        TestSuiteResult anotherTestSuite = fireCustomTestSuiteEvent(uid);
        assertEquals(testSuite, anotherTestSuite);

        TestCaseResult testCase = fireTestCaseStart(uid);
        TestCaseResult anotherTestCase = fireCustomTestCaseEvent();
        assertEquals(testCase, anotherTestCase);

        assertThat(testSuite.getTestCases(), hasSize(1));
        assertEquals(testSuite.getTestCases().get(0), testCase);

        Step parentStep = fireStepStart();
        Attachment firstAttach = fireMakeAttachment();

        assertThat(parentStep.getAttachments(), hasSize(1));
        assertEquals(parentStep.getAttachments().get(0), firstAttach);

        Step nestedStep = fireStepStart();
        Attachment secondAttach = fireMakeAttachment();

        assertFalse(firstAttach == secondAttach);

        assertThat(nestedStep.getAttachments(), hasSize(1));
        assertTrue(nestedStep.getAttachments().get(0) == secondAttach);

        fireStepFinished();

        assertThat(parentStep.getSteps(), hasSize(1));
        assertEquals(parentStep.getSteps().get(0), nestedStep);

        fireStepFinished();

        Attachment testCaseAttachment = fireMakeAttachment();

        fireTestCaseFinished();

        assertThat(testCase.getSteps(), hasSize(1));
        assertEquals(testCase.getSteps().get(0), parentStep);

        assertThat(testCase.getAttachments(), hasSize(1));
        assertEquals(testCase.getAttachments().get(0), testCaseAttachment);

        fireTestSuiteFinished(uid);
        validateTestSuite();

        assertThat(testSuite.getTestCases(), hasSize(1));

        String otherUid = UUID.randomUUID().toString();
        TestSuiteResult nextTestSuite = fireTestSuiteStart(otherUid);
        assertNotEquals(anotherTestSuite, nextTestSuite);
    }

    @Test
    public void allureClearStorageTest() {
        String suiteUid = UUID.randomUUID().toString();
        TestSuiteResult testSuite = fireTestSuiteStart(suiteUid);
        TestCaseResult testCase = fireTestCaseStart(suiteUid);
        assertThat(testSuite.getTestCases(), hasSize(1));
        assertEquals(testSuite.getTestCases().get(0), testCase);

        Step parentStep = fireStepStart();
        Step nestedStep = fireStepStart();
        fireStepFinished();

        assertThat(parentStep.getSteps(), hasSize(1));
        assertTrue(parentStep.getSteps().get(0) == nestedStep);

        fireStepFinished();
        fireClearStepStorage();

        assertThat(testCase.getSteps(), hasSize(0));
        fireClearTestStorage();
        TestCaseResult afterClearing = Allure.LIFECYCLE.getTestCaseStorage().get();
        assertFalse(testCase == afterClearing);
        checkTestCaseIsNew(afterClearing);

    }

    @Test
    public void supportForConcurrentUseOfChildThreads() throws Exception {
        final int threads = 20;
        final int stepsCount = 1000;

        final Allure lifecycle = Allure.LIFECYCLE;

        String suiteUid = UUID.randomUUID().toString();
        fireTestSuiteStart(suiteUid);
        fireTestCaseStart(suiteUid);

        ExecutorService service = Executors.newFixedThreadPool(threads);

        final List<Callable<Object>> tasks = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            tasks.add(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (int i = 0; i < stepsCount; i++) {
                        lifecycle.fire(new StepStartedEvent("test-step"));
                        lifecycle.fire(new StepFinishedEvent());
                    }
                    return "";
                }
            });
        }

        List<Future<Object>> futures = service.invokeAll(tasks);
        for (Future<Object> future : futures) {
            future.get();
        }

        assertThat(lifecycle.getStepStorage().pollLast().getSteps(), hasSize(threads * stepsCount));
    }

    @Test
    public void supportForConcurrentUseOfChildThreadsTestCases() throws Exception {
        final int threads = 20;
        final int testCases = 1000;

        final Allure lifecycle = Allure.LIFECYCLE;

        String suiteUid = UUID.randomUUID().toString();
        fireTestSuiteStart(suiteUid);
        fireTestCaseStart(suiteUid);

        ExecutorService service = Executors.newFixedThreadPool(threads);

        final List<Callable<Object>> tasks = new ArrayList<>();

        final String uid = UUID.randomUUID().toString();
        lifecycle.fire(new TestSuiteStartedEvent(uid, uid));
        for (int i = 0; i < threads; i++) {
            tasks.add(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    for (int i = 0; i < testCases; i++) {
                        lifecycle.fire(new TestCaseStartedEvent(uid, "test-case"));
                        lifecycle.fire(new StepFinishedEvent());
                    }
                    return "";
                }
            });
        }

        List<Future<Object>> futures = service.invokeAll(tasks);
        for (Future<Object> future : futures) {
            future.get();
        }

        assertThat(lifecycle.getTestSuiteStorage().get(uid).getTestCases(), hasSize(threads * testCases));
    }

    private void checkTestCaseIsNew(TestCaseResult testCaseResult) {
        assertNull(testCaseResult.getName());
        assertNull(testCaseResult.getTitle());
        assertNull(testCaseResult.getDescription());
        assertNull(testCaseResult.getFailure());
        assertNull(testCaseResult.getStatus());
        assertTrue(testCaseResult.getSteps().isEmpty());
        assertTrue(testCaseResult.getAttachments().isEmpty());
        assertTrue(testCaseResult.getLabels().isEmpty());
        assertTrue(testCaseResult.getParameters().isEmpty());
        assertTrue(testCaseResult.getStart() == 0 && testCaseResult.getStop() == 0);
    }

    private void fireClearTestStorage() {
        Allure.LIFECYCLE.fire(new ClearTestStorageEvent());
    }

    private void fireClearStepStorage() {
        Allure.LIFECYCLE.fire(new ClearStepStorageEvent());
    }

    public TestSuiteResult fireTestSuiteStart(String uid) {
        Allure.LIFECYCLE.fire(new TestSuiteStartedEvent(uid, "some.suite.name"));
        TestSuiteResult testSuite = Allure.LIFECYCLE.getTestSuiteStorage().get(uid);
        assertNotNull(testSuite);
        assertThat(testSuite.getName(), is("some.suite.name"));
        assertThat(testSuite.getTestCases(), hasSize(0));
        return testSuite;
    }

    public void fireTestSuiteFinished(String uid) {
        Allure.LIFECYCLE.fire(new TestSuiteFinishedEvent(uid));
    }

    public void validateTestSuite() throws SAXException, IOException {
        Validator validator = AllureModelUtils.getAllureSchemaValidator();

        for (File each : listTestSuiteFiles(resultsDirectory)) {
            validator.validate(new StreamSource(each));
        }
    }

    public TestCaseResult fireTestCaseStart(String uid) {
        Allure.LIFECYCLE.fire(new TestCaseStartedEvent(uid, "some.case.name"));
        TestCaseResult testCase = Allure.LIFECYCLE.getTestCaseStorage().get();
        assertNotNull(testCase);
        assertThat(testCase.getName(), is("some.case.name"));
        return testCase;
    }

    public void fireTestCaseFinished() {
        Allure.LIFECYCLE.fire(new TestCaseFinishedEvent());
    }

    public Step fireStepStart() {
        Allure.LIFECYCLE.fire(new StepStartedEvent("some.step.name"));
        Step step = Allure.LIFECYCLE.getStepStorage().getLast();
        assertNotNull(step);
        assertThat(step.getName(), is("some.step.name"));
        return step;
    }

    public Attachment fireMakeAttachment() throws UnsupportedEncodingException {
        Step lastStep = Allure.LIFECYCLE.getStepStorage().getLast();
        int attachmentsCount = lastStep.getAttachments().size();

        Allure.LIFECYCLE.fire(new MakeAttachmentEvent("some.attach.title".getBytes(UTF_8), "attach.body", ""));

        assertThat(lastStep.getAttachments().size(), is(attachmentsCount + 1));
        Attachment attachment = lastStep.getAttachments().get(attachmentsCount);
        assertNotNull(attachment);

        return attachment;
    }

    public void fireStepFinished() {
        Allure.LIFECYCLE.fire(new StepFinishedEvent());
    }

    public TestSuiteResult fireCustomTestSuiteEvent(String uid) {
        Allure.LIFECYCLE.fire(new ChangeTestSuiteTitleEvent(uid, "new.suite.title"));
        TestSuiteResult testSuite = Allure.LIFECYCLE.getTestSuiteStorage().get(uid);
        assertNotNull(testSuite);
        assertThat(testSuite.getTitle(), is("new.suite.title"));
        return testSuite;
    }

    public TestCaseResult fireCustomTestCaseEvent() {
        Allure.LIFECYCLE.fire(new ChangeTestCaseTitleEvent("new.case.title"));
        TestCaseResult testCase = Allure.LIFECYCLE.getTestCaseStorage().get();
        assertNotNull(testCase);
        assertThat(testCase.getTitle(), is("new.case.title"));
        return testCase;
    }

    @After
    public void tearDown() {
        AllureResultsUtils.setResultsDirectory(null);
    }

}
