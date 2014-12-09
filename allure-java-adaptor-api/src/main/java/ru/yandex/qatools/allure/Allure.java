package ru.yandex.qatools.allure;

import ru.yandex.qatools.allure.config.AllureConfig;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.events.ClearStepStorageEvent;
import ru.yandex.qatools.allure.events.ClearTestStorageEvent;
import ru.yandex.qatools.allure.events.RemoveAttachmentsEvent;
import ru.yandex.qatools.allure.events.StepEvent;
import ru.yandex.qatools.allure.events.StepFinishedEvent;
import ru.yandex.qatools.allure.events.StepStartedEvent;
import ru.yandex.qatools.allure.events.TestCaseEvent;
import ru.yandex.qatools.allure.events.TestCaseFinishedEvent;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;
import ru.yandex.qatools.allure.events.TestSuiteEvent;
import ru.yandex.qatools.allure.events.TestSuiteFinishedEvent;
import ru.yandex.qatools.allure.experimental.LifecycleListener;
import ru.yandex.qatools.allure.experimental.ListenersNotifier;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.Step;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.allure.storages.StepStorage;
import ru.yandex.qatools.allure.storages.TestCaseStorage;
import ru.yandex.qatools.allure.storages.TestSuiteStorage;
import ru.yandex.qatools.allure.utils.AllureShutdownHook;

import static ru.yandex.qatools.allure.utils.AllureResultsUtils.writeTestSuiteResult;

/**
 * Allure Java API. Use this class to access to Allure lifecycle
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class Allure {

    public static final Allure LIFECYCLE = new Allure();

    private static final Object TEST_SUITE_ADD_CHILD_LOCK = new Object();

    private final StepStorage stepStorage = new StepStorage();

    private final TestCaseStorage testCaseStorage = new TestCaseStorage();

    private final TestSuiteStorage testSuiteStorage = new TestSuiteStorage();

    private final ListenersNotifier notifier = new ListenersNotifier();

    /**
     * Package private. Use Allure.LIFECYCLE singleton
     */
    Allure() {
        Runtime.getRuntime().addShutdownHook(new Thread(
                new AllureShutdownHook(testSuiteStorage.getStartedSuites())
        ));
    }

    /**
     * Process StepStartedEvent. New step will be created and added to
     * stepStorage.
     *
     * @param event to process
     */
    public void fire(StepStartedEvent event) {
        Step step = new Step();
        event.process(step);
        stepStorage.put(step);

        notifier.fire(event);
    }

    /**
     * Process any StepEvent. You can change last added to stepStorage
     * step using this method.
     *
     * @param event to process
     */
    public void fire(StepEvent event) {
        Step step = stepStorage.getLast();
        event.process(step);

        notifier.fire(event);
    }

    /**
     * Process StepFinishedEvent. Change last added to stepStorage step
     * and add it as child of previous step.
     *
     * @param event to process
     */
    public void fire(StepFinishedEvent event) {
        Step step = stepStorage.adopt();
        event.process(step);

        notifier.fire(event);
    }

    /**
     * Process TestCaseStartedEvent. New testCase will be created and added
     * to suite as child.
     *
     * @param event to process
     */
    public void fire(TestCaseStartedEvent event) {
        //init root step in parent thread if needed
        stepStorage.get();

        TestCaseResult testCase = testCaseStorage.get();
        event.process(testCase);

        synchronized (TEST_SUITE_ADD_CHILD_LOCK) {
            testSuiteStorage.get(event.getSuiteUid()).getTestCases().add(testCase);
        }

        notifier.fire(event);
    }

    /**
     * Process TestCaseEvent. You can change current testCase context
     * using this method.
     *
     * @param event to process
     */
    public void fire(TestCaseEvent event) {
        TestCaseResult testCase = testCaseStorage.get();
        event.process(testCase);

        notifier.fire(event);
    }

    /**
     * Process TestCaseFinishedEvent. Add steps and attachments from
     * top step from stepStorage to current testCase, then remove testCase
     * and step from stores. Also remove attachments matches removeAttachments
     * config.
     *
     * @param event to process
     */
    public void fire(TestCaseFinishedEvent event) {
        TestCaseResult testCase = testCaseStorage.get();
        event.process(testCase);

        Step root = stepStorage.pollLast();

        if (Status.PASSED.equals(testCase.getStatus())) {
            new RemoveAttachmentsEvent(AllureConfig.newInstance().getRemoveAttachments()).process(root);
        }

        testCase.getSteps().addAll(root.getSteps());
        testCase.getAttachments().addAll(root.getAttachments());

        stepStorage.remove();
        testCaseStorage.remove();

        notifier.fire(event);
    }

    /**
     * Process TestSuiteEvent. You can use this method to change current
     * testSuite context. Using event.getUid() to access testSuite.
     *
     * @param event to process
     */
    public void fire(TestSuiteEvent event) {
        TestSuiteResult testSuite = testSuiteStorage.get(event.getUid());
        event.process(testSuite);

        notifier.fire(event);
    }

    /**
     * Process TestSuiteFinishedEvent. Using event.getUid() to access testSuite.
     * Then remove this suite from storage and marshal testSuite to xml using
     * AllureResultsUtils.writeTestSuiteResult()
     *
     * @param event to process
     */
    public void fire(TestSuiteFinishedEvent event) {
        String suiteUid = event.getUid();

        TestSuiteResult testSuite = testSuiteStorage.remove(suiteUid);
        if (testSuite == null) {
            return;
        }
        event.process(testSuite);

        testSuite.setVersion(getVersion());
        testSuite.getLabels().add(AllureModelUtils.createProgrammingLanguageLabel());

        writeTestSuiteResult(testSuite);

        notifier.fire(event);
    }

    /**
     * This method just clear current step context.
     *
     * @param event will be ignored
     */
    @SuppressWarnings("unused")
    public void fire(ClearStepStorageEvent event) {
        stepStorage.remove();

        notifier.fire(event);
    }

    /**
     * This method just clear current testCase context.
     *
     * @param event will be ignored
     */
    @SuppressWarnings("unused")
    public void fire(ClearTestStorageEvent event) {
        testCaseStorage.remove();

        notifier.fire(event);
    }

    /**
     * Experimental. Can be removed in next releases.
     * <p/>
     * Add specified listener to notifier.
     *
     * @param listener to add
     * @see ru.yandex.qatools.allure.experimental.LifecycleListener
     * @since 1.4.0
     */
    public void addListener(LifecycleListener listener) {
        notifier.addListener(listener);
    }

    /**
     * Package private. For tests only.
     *
     * @return stepStorage
     */
    StepStorage getStepStorage() {
        return stepStorage;
    }

    /**
     * Package private. For tests only.
     *
     * @return testCaseStorage
     */
    TestCaseStorage getTestCaseStorage() {
        return testCaseStorage;
    }

    /**
     * Package private. For tests only.
     *
     * @return testSuiteStorage
     */
    TestSuiteStorage getTestSuiteStorage() {
        return testSuiteStorage;
    }

    /**
     * Use this method to get Allure version in runtime. Supported since
     * version 1.3.6
     *
     * @return current Allure version
     */
    public String getVersion() {
        return AllureConfig.newInstance().getVersion();
    }
}
