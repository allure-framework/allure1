package ru.yandex.qatools.allure.data.plugins;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Main interface for all Allure report plugins.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 09.02.15
 */
public interface Plugin<T> {

    /**
     * Type of data needed to plugin
     */
    Class<T> getType();

    /**
     * Using this annotation you can specify the plugin name. This annotation
     * is required for all plugins. Plugin name should contains only latin characters
     * or numbers (but can't start with number).
     */
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Name {

        String value();
    }

    /**
     * Using this annotation you can specify plugin priority. Plugins with higher
     * priority will be processed first.
     */
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Priority {

        int value();
    }

    /**
     * This annotation helps you to specify fields with data for plugin.
     */
    @Target({ElementType.FIELD})
    @Retention(RetentionPolicy.RUNTIME)
    @interface Data {

        String value() default "##default";
    }
}
