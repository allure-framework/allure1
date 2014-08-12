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
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 26.05.14
 */
public class ListenersNotifier extends LifecycleListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private List<LifecycleListener> listeners = new ArrayList<>();

    public ListenersNotifier() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public ListenersNotifier(ClassLoader classLoader) {
        loadListeners(ServiceLoader.load(
                LifecycleListener.class,
                classLoader
        ));
    }

    private void loadListeners(ServiceLoader<LifecycleListener> loader) {
        Iterator<LifecycleListener> iterator = loader.iterator();
        while (hasNextSafely(iterator)) {
            try {
                LifecycleListener listener = iterator.next();
                listeners.add(listener);
                logger.info(String.format("Found %s: %s", LifecycleListener.class, listener.getClass()));
            } catch (ServiceConfigurationError e) {
                logger.error("iterator.next() failed", e);
            }
        }
    }

    private boolean hasNextSafely(Iterator iterator) {
        try {
            return iterator.hasNext();
        } catch (Exception e) {
            logger.error("iterator.hasNext() failed", e);
            return false;
        }
    }

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

    private void logError(LifecycleListener listener, Exception e) {
        logger.error("Error for listener " + listener.getClass(), e);
    }

    public void addListener(LifecycleListener listener) {
        listeners.add(listener);
    }

    public List<LifecycleListener> getListeners() {
        return listeners;
    }
}
