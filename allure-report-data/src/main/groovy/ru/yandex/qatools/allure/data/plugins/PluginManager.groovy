package ru.yandex.qatools.allure.data.plugins

import com.google.inject.Inject
import com.google.inject.Injector
import net.sf.corn.cps.CPScanner
import net.sf.corn.cps.ResourceFilter
import ru.yandex.qatools.allure.data.io.ReportWriter
import ru.yandex.qatools.allure.data.utils.PluginUtils

/**
 * Plugin manager helps you to find plugins and run
 * only plugins you need.
 * <p/>
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 17.02.15
 */
class PluginManager {

    public static final String PLUGINS_JSON = "plugins.json"

    protected final Storage<PreparePlugin> preparePlugins

    protected final Storage<ProcessPlugin> processPlugins

    protected final List<TabPlugin> tabPlugins

    /**
     * Create an instance of plugin manager.
     */
    @Inject
    PluginManager(PluginLoader loader, Injector injector = null) {
        def plugins = load(loader, injector)
        preparePlugins = new Storage<>(filterByType(plugins, PreparePlugin))
        processPlugins = new Storage<>(filterByType(plugins, ProcessPlugin))
        tabPlugins = filterByType(processPlugins.values().flatten(), TabPlugin)
    }

    /**
     * Find all prepare plugins an process given object for
     * each of found plugins.
     */
    public <T> void prepare(T object) {
        preparePlugins.get(object?.class)*.prepare(object)
    }

    /**
     * Find all process plugins an process given object for
     * each of found plugins.
     */
    public <T> void process(T object) {
        def plugins = processPlugins.get(object?.class)

        plugins.each {
            //copy each time, we can't use group operation
            it.process(PluginUtils.clone(object))
        }
    }

    /**
     * Write all plugin data using given report writer
     */
    public <T> void writePluginData(Class<T> type, ReportWriter writer) {
        writer.write(getData(type))
    }

    /**
     * Get plugin data for given type
     */
    protected List<PluginData> getData(Class<?> type) {
        processPlugins.get(type)*.pluginData?.flatten() as List<PluginData>
    }

    /**
     * Get list of names of tab plugins
     */
    List<String> getTabPluginNames() {
        tabPlugins.collect { plugin ->
            plugin.pluginName
        }
    }

    /**
     * Write list of tab plugins to {@link #PLUGINS_JSON}
     */
    void writePluginList(ReportWriter writer) {
        writer.write(new PluginData(PLUGINS_JSON, tabPluginNames))
    }

    /**
     * Write plugin resources. For each plugin search resources using
     * {@link #findPluginResources(ru.yandex.qatools.allure.data.plugins.ProcessPlugin)}
     *
     * @see ReportWriter
     */
    void writePluginResources(ReportWriter writer) {
        tabPlugins.each { plugin ->
            def resources = findPluginResources(plugin)
            resources.each { resource ->
                writer.write(plugin.pluginName, resource)
            }
        }
    }

    /**
     * Find all resources for plugin in current thread class loader.
     */
    protected static List<URL> findPluginResources(ProcessPlugin plugin) {
        CPScanner.scanResources(new ResourceFilter()
                .packageName(plugin.class.canonicalName)
                .resourceName("*")
        )
    }

    /**
     * Load all plugins using given {@link PluginLoader} then remove all null plugins
     * and finally inject members to each plugin in case not null injector
     */
    protected static List<Plugin> load(PluginLoader loader, Injector injector) {
        def result = [] as List<Plugin>
        loader.loadPlugins().each {
            if (isValidPlugin(it)) {
                injector?.injectMembers(it)
                result.add(it)
            }
        }
        result
    }

    /**
     * Some checks for plugins.
     * @see TabPlugin#isValid(java.lang.Class)
     */
    protected static boolean isValidPlugin(Plugin plugin) {
        return plugin && (plugin instanceof TabPlugin ? TabPlugin.isValid(plugin.class) : true)
    }

    /**
     * Find all plugins with specified type
     */
    protected static <T extends Plugin> List<T> filterByType(List<Plugin> plugins, Class<T> type) {
        plugins.findAll {
            type.isAssignableFrom(it.class)
        } as List<T>
    }

    /**
     * Internal storage for plugins.
     */
    protected class Storage<T extends Plugin> extends HashMap<Class, List<T>> {
        Storage(List<T> plugins) {
            plugins.each {
                containsKey(it.type) ? get(it.type).add(it) : put(it.type, [it])
            }
        }
    }
}
