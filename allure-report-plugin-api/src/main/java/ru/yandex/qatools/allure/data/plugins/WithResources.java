package ru.yandex.qatools.allure.data.plugins;

/**
 * Common interface for all plugins with some resources. You can pass resources
 * to the report by adding it to class path to ${yourPluginCanonicalClassName} folder.
 * These resources  will be copied to plugins/${pluginName} folder and added to
 * plugins.json. Then plugins/${pluginName}/script.js will be loaded.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 22.04.15
 * @see AbstractPlugin
 */
public interface WithResources {

    /**
     * Name for plugin. Name should be unique and contains only latin characters.
     */
    String getName();


}
