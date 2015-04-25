package ru.yandex.qatools.allure.data.plugins

import groovy.transform.CompileStatic
import groovy.transform.PackageScope

/**
 * Internal storage for plugins.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 25.04.15
 */
@PackageScope
@CompileStatic
class PluginStorage<T extends Plugin> extends HashMap<Class, List<T>> {
    PluginStorage(List<T> plugins) {
        for (T plugin : plugins) {
            containsKey(plugin.type) ? get(plugin.type).add(plugin) : put(plugin.type, [plugin])
        }
    }
}
