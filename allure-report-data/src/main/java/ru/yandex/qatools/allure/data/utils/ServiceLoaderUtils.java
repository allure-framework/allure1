package ru.yandex.qatools.allure.data.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 07.07.14
 *         <p/>
 *         Load services by specified service type using Java SPI.
 */
public final class ServiceLoaderUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLoaderUtils.class);

    /**
     * Don't use instance of this class
     */
    ServiceLoaderUtils() {
    }

    /**
     * Invoke to find all services for given service type using specified class loader
     *
     * @param classLoader specified class loader
     * @param serviceType given service type
     * @return List of found services
     */
    public static <T> List<T> load(ClassLoader classLoader, Class<T> serviceType) {
        List<T> foundServices = new ArrayList<>();
        Iterator<T> iterator = ServiceLoader.load(serviceType, classLoader).iterator();

        while (checkHasNextSafely(iterator)) {
            try {
                T item = iterator.next();
                foundServices.add(item);
                LOGGER.debug(String.format("Found %s [%s]", serviceType.getSimpleName(), item.toString()));
            } catch (ServiceConfigurationError e) {
                LOGGER.trace("Can't find services using Java SPI", e);
                LOGGER.error(e.getMessage());
            }
        }
        return foundServices;
    }

    /**
     * Check {@link java.util.Iterator#hasNext()} safely.
     *
     * @param iterator specified Iterator to check hasNext
     * @return true if {@link java.util.Iterator#hasNext()} checked successfully, false otherwise.
     */
    public static boolean checkHasNextSafely(Iterator iterator) {
        try {
            /* Throw a ServiceConfigurationError if a provider-configuration file violates the specified format,
            or if it names a provider class that cannot be found and instantiated, or if the result of
            instantiating the class is not assignable to the service type, or if any other kind of exception
            or error is thrown as the next provider is located and instantiated.
            @see http://docs.oracle.com/javase/7/docs/api/java/util/ServiceLoader.html#iterator()
            */
            return iterator.hasNext();
        } catch (Exception | ServiceConfigurationError e) {
            LOGGER.trace("Can't load some service using Java SPI", e);
            LOGGER.error(e.getMessage());
            return false;
        }
    }

}
