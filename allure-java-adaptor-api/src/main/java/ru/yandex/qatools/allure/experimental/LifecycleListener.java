package ru.yandex.qatools.allure.experimental;

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

/**
 * Warning: Experimental Allure feature. Can be removed in next releases.
 * <p/>
 * If you need to respond to the Allure events during test run, extend {@link LifecycleListener} and
 * override the appropriate methods. If a listener throws an exception while processing a Allure
 * event, it will be removed for the remainder of the test run.
 * <p/>
 * An example, if you want to log each step, you could write:
 * <pre>
 * public class EachStepLogger extends LifecycleListener {
 *
 *     private Deque<String> names = new LinkedList<>();
 *
 *     &#064;Override
 *     public void fire(StepStartedEvent event) {
 *         names.push(event.getName());
 *         System.out.println(getOffset() + "step started " + names.getFirst());
 *     }
 *
 *     &#064;Override
 *     public void fire(StepFinishedEvent event) {
 *         System.out.println(getOffset() + "step finished " + names.poll());
 *     }
 *
 *     private String getOffset() {
 *         return new String(new char[names.size() == 0 ? 0 : names.size() - 1]).replaceAll("\0", "   ");
 *     }
 * }
 * </pre>
 * To add your listener you should use Java SPI https://docs.oracle.com/javase/tutorial/ext/basics/spi.html
 * or {@link ru.yandex.qatools.allure.Allure#addListener(LifecycleListener)} method
 * <p/>
 * All listener methods will be invoked after event processing. Do not change given events, it can affect others
 * listeners.
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 26.05.14
 */
public abstract class LifecycleListener {

    /**
     * Called when a step started
     */
    public void fire(StepStartedEvent event) { // NOSONAR
    }

    /**
     * Called when Allure process any custom step event, such as
     * {@link ru.yandex.qatools.allure.events.MakeAttachmentEvent}
     */
    public void fire(StepEvent event) { // NOSONAR
    }

    /**
     * Called when a step finished
     */
    public void fire(StepFinishedEvent event) { // NOSONAR
    }

    /**
     * Called when a test case started
     */
    public void fire(TestCaseStartedEvent event) { // NOSONAR
    }

    /**
     * Called when Allure process any custom test case event, such as
     * {@link ru.yandex.qatools.allure.events.AddParameterEvent}
     */
    public void fire(TestCaseEvent event) { // NOSONAR
    }

    /**
     * Called when a test case finished
     */
    public void fire(TestCaseFinishedEvent event) { // NOSONAR
    }

    /**
     * Called when Allure process any custom test suite event
     */
    public void fire(TestSuiteEvent event) { // NOSONAR
    }

    /**
     * Called when a test suite finished
     */
    public void fire(TestSuiteFinishedEvent event) { // NOSONAR
    }

    /**
     * Called when Allure clear current step context.
     */
    public void fire(ClearStepStorageEvent event) { // NOSONAR
    }

    /**
     * Called when Allure clear current test case context.
     */
    public void fire(ClearTestStorageEvent event) { // NOSONAR
    }
}
