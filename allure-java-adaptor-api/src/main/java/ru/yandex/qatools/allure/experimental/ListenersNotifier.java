package ru.yandex.qatools.allure.experimental;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/**
 * This is an internal Allure component.
 * <p/>
 * {@link ru.yandex.qatools.allure.experimental.ListenersNotifier} used for notify
 * listeners {@link ru.yandex.qatools.allure.experimental.LifecycleListener} of
 * Allure events.
 * <p/>
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 26.05.14
 * @see ru.yandex.qatools.allure.Allure
 */
public class ListenersNotifier extends LifecycleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListenersNotifier.class);

    private List<LifecycleListener> listeners = new ArrayList<>();

    /**
     * Create instance of {@link ru.yandex.qatools.allure.experimental.ListenersNotifier}
     * using current context class loader.
     */
    public ListenersNotifier() {
        this(Thread.currentThread().getContextClassLoader());
    }

    /**
     * Create instance of {@link ru.yandex.qatools.allure.experimental.ListenersNotifier}
     * using specified class loader. This class loader will be used to find listeners
     * via Java SPI.
     *
     * @param classLoader given class loader to find {@link ru.yandex.qatools.allure.experimental.LifecycleListener}
     *                    services
     */
    public ListenersNotifier(ClassLoader classLoader) {
        loadListeners(ServiceLoader.load(
                LifecycleListener.class,
                classLoader
        ));
    }

    /**
     * Safely find all {@link ru.yandex.qatools.allure.experimental.LifecycleListener}
     * services.
     *
     * @param loader specified {@link java.util.ServiceLoader} to find our services
     */
    private void loadListeners(ServiceLoader<LifecycleListener> loader) {
        Iterator<LifecycleListener> iterator = loader.iterator();
        while (hasNextSafely(iterator)) {
            try {
                LifecycleListener listener = iterator.next();
                listeners.add(listener);
                LOGGER.info(String.format("Found %s: %s", LifecycleListener.class, listener.getClass()));
            } catch (ServiceConfigurationError e) {
                LOGGER.error("iterator.next() failed", e);
            }
        }
    }

    /**
     * Safely check for <pre>iterator.hasNext()</pre>.
     *
     * @param iterator specified iterator to check he presence of next element
     * @return {@code true} if the iteration has more elements, false otherwise
     */
    private boolean hasNextSafely(Iterator iterator) {
        try {
            /* Throw a ServiceConfigurationError if a provider-configuration file violates the specified format,
+            or if it names a provider class that cannot be found and instantiated, or if the result of
+            instantiating the class is not assignable to the service type, or if any other kind of exception
+            or error is thrown as the next provider is located and instantiated.
+            @see http://docs.oracle.com/javase/7/docs/api/java/util/ServiceLoader.html#iterator()
+            */
            return iterator.hasNext();
        } catch (Exception e) {
            LOGGER.error("iterator.hasNext() failed", e);
            return false;
        }
    }

    /**
     * Invoke to tell listeners that an step started event processed
     */
    @Override
    public void fire(StepStartedEvent event) {
        for (LifecycleListener listener : listeners) {
            try {
                listener.fire(event);
            } catch (Exception e) {
                logError(listener, e);
            }
        }
    }

    /**
     * Invoke to tell listeners that an custom step event processed
     */
    @Override
    public void fire(StepEvent event) {
        for (LifecycleListener listener : listeners) {
            try {
                listener.fire(event);
            } catch (Exception e) {
                logError(listener, e);
            }
        }
    }

    /**
     * Invoke to tell listeners that an step finished event processed
     */
    @Override
    public void fire(StepFinishedEvent event) {
        for (LifecycleListener listener : listeners) {
            try {
                listener.fire(event);
            } catch (Exception e) {
                logError(listener, e);
            }
        }
    }

    /**
     * Invoke to tell listeners that an test case started event processed
     */
    @Override
    public void fire(TestCaseStartedEvent event) {
        for (LifecycleListener listener : listeners) {
            try {
                listener.fire(event);
            } catch (Exception e) {
                logError(listener, e);
            }
        }
    }

    /**
     * Invoke to tell listeners that an custom test case event processed
     */
    @Override
    public void fire(TestCaseEvent event) {
        for (LifecycleListener listener : listeners) {
            try {
                listener.fire(event);
            } catch (Exception e) {
                logError(listener, e);
            }
        }
    }

    /**
     * Invoke to tell listeners that an test case finished event processed
     */
    @Override
    public void fire(TestCaseFinishedEvent event) {
        for (LifecycleListener listener : listeners) {
            try {
                listener.fire(event);
            } catch (Exception e) {
                logError(listener, e);
            }
        }
    }

    /**
     * Invoke to tell listeners that an custom test suite event processed
     */
    @Override
    public void fire(TestSuiteEvent event) {
        for (LifecycleListener listener : listeners) {
            try {
                listener.fire(event);
            } catch (Exception e) {
                logError(listener, e);
            }
        }
    }

    /**
     * Invoke to tell listeners that an test suite finished event processed
     */
    @Override
    public void fire(TestSuiteFinishedEvent event) {
        for (LifecycleListener listener : listeners) {
            try {
                listener.fire(event);
            } catch (Exception e) {
                logError(listener, e);
            }
        }
    }

    /**
     * Invoke to tell listeners that an clear step storage event processed
     */
    @Override
    public void fire(ClearStepStorageEvent event) {
        for (LifecycleListener listener : listeners) {
            try {
                listener.fire(event);
            } catch (Exception e) {
                logError(listener, e);
            }
        }
    }

    /**
     * Invoke to tell listeners that an clear test case storage event processed
     */
    @Override
    public void fire(ClearTestStorageEvent event) {
        for (LifecycleListener listener : listeners) {
            try {
                listener.fire(event);
            } catch (Exception e) {
                logError(listener, e);
            }
        }
    }

    /**
     * This method log given exception in specified listener
     */
    private void logError(LifecycleListener listener, Exception e) {
        LOGGER.error("Error for listener " + listener.getClass(), e);
    }

    /**
     * You can use this method to add listeners to this notifier.
     */
    public void addListener(LifecycleListener listener) {
        listeners.add(listener);
    }

    /**
     * Getter for {@link #listeners}
     */
    public List<LifecycleListener> getListeners() {
        return listeners;
    }
}
