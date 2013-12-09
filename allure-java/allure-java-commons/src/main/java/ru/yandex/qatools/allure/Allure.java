package ru.yandex.qatools.allure;

import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.events.*;
import ru.yandex.qatools.allure.exceptions.UnknownEventException;
import ru.yandex.qatools.allure.model.*;
import ru.yandex.qatools.allure.model.Step;
import ru.yandex.qatools.allure.storages.TestRunStorage;
import ru.yandex.qatools.allure.storages.TestStepStorage;
import ru.yandex.qatools.allure.storages.TestStorage;
import ru.yandex.qatools.allure.utils.AllureWriteUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;

import static ru.yandex.qatools.allure.utils.AllureWriteUtils.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public enum Allure {

    LIFECYCLE;

    private static final Object LOCK = new Object();

    private Allure() {
    }

    public void fire(Event event) {
        if (event instanceof TestRunStartedEvent) {
            fireTestRunStartedEvent((TestRunStartedEvent) event);
        } else if (event instanceof TestRunFinishedEvent) {
            fireTestRunFinishedEvent((TestRunFinishedEvent) event);
        } else if (event instanceof TestStartedEvent) {
            fireTestStartedEvent((TestStartedEvent) event);
        } else if (event instanceof TestFinishedEvent) {
            fireTestFinishedEvent((TestFinishedEvent) event);
        } else if (event instanceof TestFailureEvent) {
            fireTestFailureEvent((TestFailureEvent) event);
        } else if (event instanceof TestAssumptionFailureEvent) {
            fireTestAssumptionFailureEvent((TestAssumptionFailureEvent) event);
        } else if (event instanceof StepStartEvent) {
            fireStepStartEvent((StepStartEvent) event);
        } else if (event instanceof StepStopEvent) {
            fireStepStopEvent((StepStopEvent) event);
        } else if (event instanceof StepFailureEvent) {
            fireStepFailureEvent((StepFailureEvent) event);
        } else if (event instanceof MakeAttachEvent) {
            fireMakeAttachEvent((MakeAttachEvent) event);
        } else {
            throw new UnknownEventException("Unknown event " + event);
        }
    }

    private void fireTestRunStartedEvent(TestRunStartedEvent event) {
        TestSuiteResult testSuite = TestRunStorage.getTestRun(event.getUid());
        testSuite.setStart(System.currentTimeMillis());
        String simpleName = event.getTestRunName().replaceAll(".*\\.(\\S+)", "$1");
        testSuite.setTitle(AllureWriteUtils.humanize(simpleName));
        testSuite.setClassname(event.getTestRunName());

        for (Annotation annotation : event.getAnnotations()) {
            if (annotation instanceof Title) {
                testSuite.setTitle(((Title) annotation).value());
            }
            if (annotation instanceof Description) {
                testSuite.setDescription(((Description) annotation).value());
            }
        }
    }

    private void fireTestRunFinishedEvent(TestRunFinishedEvent event) {
        TestSuiteResult testSuite = TestRunStorage.pollTestRun(event.getUid());
        testSuite.setStop(System.currentTimeMillis());
        marshalTestSuite(testSuite);
    }

    private void fireTestStartedEvent(TestStartedEvent event) {
        TestCaseResult testCase = TestStorage.getTest(event.getUid());
        testCase.setStart(System.currentTimeMillis());
        testCase.setTitle(humanize(event.getTestName()));
        testCase.setSeverity(SeverityLevel.NORMAL);
        testCase.setStatus(Status.PASSED);

        for (Annotation annotation : event.getAnnotations()) {
            if (annotation instanceof Title) {
                testCase.setTitle(((Title) annotation).value());
            }
            if (annotation instanceof Description) {
                testCase.setDescription(((Description) annotation).value());
            }
            if (annotation instanceof Severity) {
                testCase.setSeverity(((Severity) annotation).value());
            }
            if (annotation instanceof Story) {
                Story story = (Story) annotation;
                for (Class<?> clazz : story.value()) {
                    if (clazz.isAnnotationPresent(StoryClass.class)
                            && clazz.getDeclaringClass().isAnnotationPresent(FeatureClass.class)) {
                        Label labelStory = new Label();
                        labelStory.setName("Story");
                        labelStory.setValue(clazz.getSimpleName());

                        Label labelFeature = new Label();
                        labelFeature.setName("Feature");
                        labelFeature.setValue(clazz.getDeclaringClass().getSimpleName());
                        testCase.getLabels().add(labelStory);
                        testCase.getLabels().add(labelFeature);
                    }
                }
            }
        }
    }

    private void fireTestFinishedEvent(TestFinishedEvent event) {
        TestCaseResult testCase = TestStorage.pollTest(event.getUid());
        testCase.setStop(System.currentTimeMillis());

        Step rootStep = TestStepStorage.pollTestStep();

        testCase.getSteps().addAll(rootStep.getSteps());
        testCase.getAttachments().addAll(rootStep.getAttachments());

        TestSuiteResult testSuiteResult = TestRunStorage.getTestRun(event.getRunUid());
        synchronized (LOCK) {
            testSuiteResult.getTestCases().add(testCase);

        }
    }

    private void fireTestFailureEvent(TestFailureEvent event) {
        TestCaseResult testCase = TestStorage.getTest(event.getUid());
        testCase.setStatus(getStatusByThrowable(event.getThrowable()));
        testCase.setFailure(getFailureByThrowable(event.getThrowable()));
    }

    private void fireTestAssumptionFailureEvent(TestAssumptionFailureEvent event) {
        TestCaseResult testCase = TestStorage.getTest(event.getUid());
        testCase.setStatus(Status.SKIPPED);
        testCase.setFailure(getFailureByThrowable(event.getThrowable()));
    }

    private void fireStepStartEvent(StepStartEvent event) {
        Step step = new Step();
        step.setTitle(event.getStepTitle());
        step.setStatus(Status.PASSED);
        step.setStart(System.currentTimeMillis());
        TestStepStorage.putTestStep(step);
    }

    @SuppressWarnings("unused")
    private void fireStepStopEvent(StepStopEvent event) {
        Step step = TestStepStorage.pollTestStep();
        step.setStop(System.currentTimeMillis());

        Step parentStep = TestStepStorage.getTestStep();
        parentStep.getSteps().add(step);

    }

    private void fireStepFailureEvent(StepFailureEvent event) {
        Step step = TestStepStorage.getTestStep();
        step.setStatus(getStatusByThrowable(event.getThrowable()));
    }

    private void fireMakeAttachEvent(MakeAttachEvent event) {
        Attachment attachment = new Attachment();
        attachment.setTitle(event.getTitle());
        attachment.setType(event.getAttachmentType());
        attachment.setSource(AllureWriteUtils.writeAttachment(
                event.getAttach(),
                event.getAttachmentType(),
                ".attach")
        );

        Step step = TestStepStorage.getTestStep();
        if (step.getAttachments() == null) {
            step.setAttachments(new ArrayList<Attachment>());
        }
        step.getAttachments().add(attachment);

    }

    private static Status getStatusByThrowable(Throwable throwable) {
        if (throwable instanceof AssertionError) {
            return Status.FAILED;
        } else {
            return Status.BROKEN;
        }
    }

    private static Failure getFailureByThrowable(Throwable throwable) {
        Failure failure = new Failure();
        if (throwable.getMessage() == null || throwable.getMessage().length() == 0) {
            failure.setMessage(throwable.getClass().getName());
        } else {
            failure.setMessage(throwable.getMessage());
        }
        StringWriter writer = new StringWriter();
        PrintWriter printer = new PrintWriter(writer);
        throwable.printStackTrace(printer);
        failure.setStackTrace(writer.toString());
        return failure;
    }
}
