package ru.yandex.qatools.allure;

import ru.yandex.qatools.allure.events.*;
import ru.yandex.qatools.allure.model.*;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.24.13
 */

@SuppressWarnings("unused")
public enum Controller {

    ALLURE_LIFECYCLE;

    private Controller() {
    }

    private static final Object LOCK = new Object();

    public void stepStart(String stepTitle) {
        Allure.LIFECYCLE.fire(new StepStartEvent(stepTitle));
    }

    public void stepStop() {
        Allure.LIFECYCLE.fire(new StepStopEvent());
    }

    public void makeAttach(String title, AttachmentType attachmentType, Object attach) {
        Allure.LIFECYCLE.fire(new MakeAttachEvent(title, attachmentType, attach));
    }

    public void stepFailure(Throwable throwable) {
        Allure.LIFECYCLE.fire(new StepFailureEvent(throwable));
    }

    public void testRunStarted(String uid, String testRunName, Collection<Annotation> annotations) {
        Allure.LIFECYCLE.fire(new TestRunStartedEvent(uid, testRunName, annotations));
    }

    public void testRunFinished(String uid) {
        Allure.LIFECYCLE.fire(new TestRunFinishedEvent(uid));
    }

    public void testStarted(String uid, String testName, Collection<Annotation> annotations) {
        Allure.LIFECYCLE.fire(new TestStartedEvent(uid, testName, annotations));
    }

    public void testFinished(String runUid, String uid) {
        Allure.LIFECYCLE.fire(new TestFinishedEvent(runUid, uid));
    }

    public void testFailure(String uid, Throwable e) {
        Allure.LIFECYCLE.fire(new TestFailureEvent(uid, e));
    }

    public void testAssumptionFailure(String uid, Throwable e) {
        Allure.LIFECYCLE.fire(new TestAssumptionFailureEvent(uid, e));
    }
}
