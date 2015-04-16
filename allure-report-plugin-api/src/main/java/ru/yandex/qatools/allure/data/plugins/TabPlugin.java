package ru.yandex.qatools.allure.data.plugins;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 16.04.15
 */
public interface TabPlugin<T> extends ProcessPlugin<T> {

    /**
     * Returns the name of the plugin
     * @return
     */
    String getName();
}
