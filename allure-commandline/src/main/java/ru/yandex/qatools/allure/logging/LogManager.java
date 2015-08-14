package ru.yandex.qatools.allure.logging;

import ch.qos.cal10n.MessageConveyor;
import org.slf4j.cal10n.LocLogger;
import org.slf4j.cal10n.LocLoggerFactory;
import ru.qatools.properties.PropertyLoader;
import ru.yandex.qatools.allure.CommandProperties;

import java.util.Locale;

/**
 * @author Artem Eroshenko <eroshenkoam@yandex-team.ru>
 */
public class LogManager {

    public static final String LOCALE_TAG = PropertyLoader.newInstance().
            populate(CommandProperties.class).getAllureLocale();

    private LogManager() {
    }

    public static Locale getLocale() {
        return Locale.forLanguageTag(LOCALE_TAG);
    }

    public static LocLogger getLogger(Class clazz) {
        return getLogger(clazz, getLocale());
    }

    public static LocLogger getLogger(Class clazz, Locale locale) {
        MessageConveyor conveyor = new MessageConveyor(locale);
        LocLoggerFactory factory = new LocLoggerFactory(conveyor);
        return factory.getLocLogger(clazz);
    }

    public static void shutdown() {
        org.apache.log4j.LogManager.shutdown();
    }


}
