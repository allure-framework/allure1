package ru.yandex.qatools.allure.data.plugins;

import java.util.List;

/**
 * You can use this index to find loaded plugins.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.07.15
 */
public interface PluginsIndex {

    /**
     * Find the plugin by given type. If there is few
     * plugins with given type returns the some of them.
     *
     * @param type the given type of plugin to find.
     * @param <T>  the plugin type to find.
     * @return any loaded plugin with given type or null if
     * there is no such plugin.
     */
    <T> T find(Class<T> type);

    /**
     * Find all the plugins with type.
     *
     * @param type the given type of plugin to find.
     * @param <T>  the plugin type to find.
     * @return all loaded plugins with given type or null if
     * there is no such plugins.
     */
    <T> List<T> findAll(Class<T> type);

    /**
     * Find plugin by name. See {@link ru.yandex.qatools.allure.data.plugins.Plugin.Name}
     * Returns null if there is no such plugin.
     *
     * @param name the given name of plugin to find.
     * @return found plugin with given name or null if there is no such plugin.
     */
    Plugin find(String name);

    /**
     * Get all loaded plugins.
     *
     * @return the list of loaded plguins.
     */
    List<Plugin> getPlugins();

}
