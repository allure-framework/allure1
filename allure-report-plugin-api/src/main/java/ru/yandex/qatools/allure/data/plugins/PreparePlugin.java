package ru.yandex.qatools.allure.data.plugins;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 09.02.15
 */
public interface PreparePlugin<T> extends Plugin {

    /**
     * Type of data needed to plugin
     */
    Class<T> getType();

    /**
     * Prepare all data with specified type.
     */
    void prepare(T data);

}
