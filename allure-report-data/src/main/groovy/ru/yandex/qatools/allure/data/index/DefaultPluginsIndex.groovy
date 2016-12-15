package ru.yandex.qatools.allure.data.index

import com.google.inject.Injector
import com.google.inject.Singleton
import ru.yandex.qatools.allure.data.plugins.AbstractPlugin
import ru.yandex.qatools.allure.data.plugins.Plugin
import ru.yandex.qatools.allure.data.plugins.PluginLoader
import ru.yandex.qatools.allure.data.plugins.PluginsIndex
import ru.yandex.qatools.allure.data.plugins.WithPriority
import ru.yandex.qatools.allure.data.plugins.WithResources

import static java.util.Collections.unmodifiableList

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.07.15
 */
@Singleton
class DefaultPluginsIndex implements PluginsIndex {

    List<Plugin> plugins

    /**
     * Creates an instance of plugin index from given plugins. Plugins were
     * sorted by priority. Plugins with higher priority will be first,
     * if some plugins have equals priority then they will be ordered alphabetically.
     */
    DefaultPluginsIndex(List<Plugin> plugins) {
        this.plugins = plugins ? unmodifiableList(plugins
                .findAll { it }
                .sort(false, sortClosure)
        ) : []
    }

    /**
     * @inheritDoc
     */
    @Override
    def <T> T find(Class<T> type) {
        filterByType(plugins, type).find()
    }

    /**
     * @inheritDoc
     */
    @Override
    def <T> List<T> findAll(Class<T> type) {
        unmodifiableList(filterByType(plugins, type))
    }

    /**
     * @inheritDoc
     */
    @Override
    Plugin find(String name) {
        filterByType(plugins, WithResources).find { it.name == name } as Plugin
    }

    /**
     * @inheritDoc
     */
    @Override
    List<Plugin> getPlugins() {
        return unmodifiableList(plugins)
    }

    /**
     * Load all plugins using given {@link PluginLoader} then remove all null plugins
     * and finally inject members to each plugin in case not null injector. Plugins
     * returned in order by priority. Plugins with higher priority will be first,
     * if some plugins have equals priority then they will be ordered alphabetically.
     */
    protected static List<Plugin> load(PluginLoader loader, Injector injector) {
        def result = [] as List<Plugin>

        def plugins = loader.loadPlugins() ?: [] as List<Plugin>
        plugins.each {
            if (isValidPlugin(it)) {
                injector?.injectMembers(it)
                result.add(it)
            }
        }
        result.sort(false, { first, second ->
            getPriority(second as Plugin) <=> getPriority(first as Plugin) ?:
                    first.class.simpleName <=> second.class.simpleName
        })
    }

    /**
     * Get priority of given plugin.
     * @see ru.yandex.qatools.allure.data.plugins.WithPriority
     */
    protected static int getPriority(Plugin plugin) {
        plugin instanceof WithPriority ? (plugin as WithPriority).priority : 0
    }

    /**
     * Some checks for plugins.
     * @see ru.yandex.qatools.allure.data.plugins.AbstractPlugin#isValid(java.lang.Class)
     */
    protected static boolean isValidPlugin(Plugin plugin) {
        return plugin && (plugin instanceof AbstractPlugin ?
                AbstractPlugin.isValid(plugin.class) : true)
    }

    /**
     * Find all plugins with specified type.
     */
    protected static <T> List<T> filterByType(List<? extends Plugin> plugins, Class<T> type) {
        plugins.findAll {
            type.isAssignableFrom((it as Object).class)
        } as List<T>
    }

    /**
     * Returns the closure to sort plugins by priority and alphabetically in case equals priority.
     */
    protected static Closure<Integer> getSortClosure() {
        { first, second ->
            getPriority(second as Plugin) <=> getPriority(first as Plugin) ?:
                    first.class.simpleName <=> second.class.simpleName
        }
    }
}
