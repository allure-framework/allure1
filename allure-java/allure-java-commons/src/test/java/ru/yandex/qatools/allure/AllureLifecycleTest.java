package ru.yandex.qatools.allure;

import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.xml.sax.SAXException;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.events.*;
import ru.yandex.qatools.allure.model.*;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static ru.yandex.qatools.allure.config.AllureNamingUtils.listTestSuiteFiles;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.12.13
 */
public class AllureLifecycleTest {

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    public static File resultsDirectory;

    @BeforeClass
    public static void setUp() throws Exception {
        resultsDirectory = folder.newFolder();
        System.setProperty("allure.results.directory", resultsDirectory.getAbsolutePath());
    }

    @Test
    public void allureLifecycleTest() throws Exception {
        TestSuiteResult testSuite = fireTestSuiteStart();
        TestSuiteResult anotherTestSuite = fireCustomTestSuiteEvent();
        assertEquals(testSuite, anotherTestSuite);

        TestCaseResult testCase = fireTestCaseStart();
        TestCaseResult anotherTestCase = fireCustomTestCaseEvent();
        assertEquals(testCase, anotherTestCase);

        assertThat(testSuite.getTestCases(), hasSize(1));
        assertEquals(testSuite.getTestCases().get(0), testCase);

        Step parentStep = fireStepStart();
        Attachment firstAttach = fireMakeAttach();

        assertThat(parentStep.getAttachments(), hasSize(1));
        assertEquals(parentStep.getAttachments().get(0), firstAttach);

        Step nestedStep = fireStepStart();
        Attachment secondAttach = fireMakeAttach();

        assertNotEquals(firstAttach, secondAttach);

        assertThat(nestedStep.getAttachments(), hasSize(1));
        assertEquals(nestedStep.getAttachments().get(0), secondAttach);

        fireStepFinished();

        assertThat(parentStep.getSteps(), hasSize(1));
        assertEquals(parentStep.getSteps().get(0), nestedStep);

        fireStepFinished();

        Attachment testCaseAttachment = fireMakeAttach();

        fireTestCaseFinished();

        assertThat(testCase.getSteps(), hasSize(1));
        assertEquals(testCase.getSteps().get(0), parentStep);

        assertThat(testCase.getAttachments(), hasSize(1));
        assertEquals(testCase.getAttachments().get(0), testCaseAttachment);

        fireTestSuiteFinished();
        validateTestSuite();

        assertThat(testSuite.getTestCases(), hasSize(1));

        TestSuiteResult nextTestSuite = fireTestSuiteStart();
        assertNotEquals(anotherTestSuite, nextTestSuite);
    }

    public TestSuiteResult fireTestSuiteStart() {
        Allure.LIFECYCLE.fire(new TestSuiteStartedEvent("some.uid", "some.suite.name"));
        TestSuiteResult testSuite = Allure.LIFECYCLE.getTestSuiteStorage().get("some.uid");
        assertNotNull(testSuite);
        assertThat(testSuite.getName(), is("some.suite.name"));
        assertThat(testSuite.getTestCases(), hasSize(0));
        return testSuite;
    }

    public void fireTestSuiteFinished() {
        Allure.LIFECYCLE.fire(new TestSuiteFinishedEvent("some.uid"));
    }

    public void validateTestSuite() throws SAXException, IOException {
        Validator validator = AllureModelUtils.getAllureSchemaValidator();

        for (File each : listTestSuiteFiles(resultsDirectory)) {
            validator.validate(new StreamSource(each));
        }
    }

    public TestCaseResult fireTestCaseStart() {
        Allure.LIFECYCLE.fire(new TestCaseStartedEvent("some.uid", "some.case.name"));
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

    public Attachment fireMakeAttach() {
        Step lastStep = Allure.LIFECYCLE.getStepStorage().getLast();
        int attachmentsCount = lastStep.getAttachments().size();

        Allure.LIFECYCLE.fire(new MakeAttachEvent("some.attach.title", AttachmentType.TXT, "attach.body"));

        assertThat(lastStep.getAttachments().size(), is(attachmentsCount + 1));
        Attachment attachment = lastStep.getAttachments().get(attachmentsCount);
        assertNotNull(attachment);

        return attachment;
    }

    public void fireStepFinished() {
        Allure.LIFECYCLE.fire(new StepFinishedEvent());
    }

    public TestSuiteResult fireCustomTestSuiteEvent() {
        Allure.LIFECYCLE.fire(new ChangeTestSuiteTitleEvent("some.uid", "new.suite.title"));
        TestSuiteResult testSuite = Allure.LIFECYCLE.getTestSuiteStorage().get("some.uid");
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

    @AfterClass
    public static void tearDown() {
        System.setProperty("allure.results.directory", "");
    }

}
