package ru.yandex.qatools.allure.data.plugins

import com.google.common.reflect.ClassPath
import com.google.inject.Inject
import com.google.inject.Injector
import groovy.transform.CompileStatic
import ru.yandex.qatools.allure.data.io.ReportWriter
import ru.yandex.qatools.allure.data.utils.PluginUtils

/**
 * Plugin manager helps you to find plugins and run
 * only plugins you need.
 * <p/>
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 17.02.15
 */
@CompileStatic
class PluginManager {

    /**
     * File with this name contains list of plugins with resources.
     */
    public static final String PLUGINS_JSON = "plugins.json"
    public static final String WIDGETS_JSON = "widgets.json"

    protected final PluginStorage<PreparePlugin> preparePlugins

    protected final PluginStorage<ProcessPlugin> processPlugins

    protected final List<WithResources> pluginsWithResources

    protected final List<WithWidget> pluginsWithWidgets

    protected final List<WithData> pluginsWithData

    /**
     * Create an instance of plugin manager.
     */
    @Inject
    PluginManager(PluginLoader loader, Injector injector = null) {
        def plugins = load(loader, injector)
        preparePlugins = new PluginStorage<>(filterByType(plugins, PreparePlugin))

        def processors = filterByType(plugins, ProcessPlugin)
        processPlugins = new PluginStorage<>(processors)
        pluginsWithResources = filterByType(processors, WithResources)
        pluginsWithWidgets = filterByType(processors, WithWidget)
        pluginsWithData = filterByType(processors, WithData)
    }

    /**
     * Get list of names of plugins with resources.
     */
    List<String> getPluginsNames() {
        pluginsWithResources.collect { plugin ->
            plugin.name
        }
    }

    /**
     * Get all data for plugins with data.
     */
    List<PluginData> getPluginsData() {
        pluginsWithData*.pluginData.flatten() as List<PluginData>
    }

    /**
     * Get all plugin widgets.
     */
    List<Widget> getWidgets() {
        pluginsWithWidgets*.widget
    }

    /**
     * Find all prepare plugins an process given object for
     * each of found plugins.
     */
    public <T> void prepare(T object) {
        def plugins = (preparePlugins.get(object?.class) ?: []) as List<PreparePlugin>
        plugins*.prepare(object)
    }

    /**
     * Find all process plugins an process given object for
     * each of found plugins.
     */
    public <T> void process(T object) {
        def plugins = (processPlugins.get(object?.class) ?: []) as List<ProcessPlugin>
        for (def plugin : plugins) {
            plugin.process(PluginUtils.clone(object))
        }
    }

    /**
     * Write list of plugins with resources to {@link #PLUGINS_JSON}
     *
     * @see ReportWriter
     */
    void writePluginList(ReportWriter writer) {
        writer.write(new PluginData(PLUGINS_JSON, pluginsNames))
    }

    /**
     * Write plugins widgets to {@link #WIDGETS_JSON}.
     *
     * @see ReportWriter
     */
    void writePluginWidgets(ReportWriter writer) {
        writer.write(new PluginData(WIDGETS_JSON, widgets))
    }

    /**
     * Write plugin resources. For each plugin search resources using
     * {@link #findPluginResources(WithResources)}
     *
     * @see ReportWriter
     */
    void writePluginResources(ReportWriter writer) {
        pluginsWithResources.each { plugin ->
            def resources = findPluginResources(plugin)
            resources.each { resource ->
                writer.write(plugin.name, resource)
            }
        }
    }

    /**
     * Write plugins data to data directory.
     *
     * @see ReportWriter
     */
    void writePluginData(ReportWriter writer) {
        writer.write(pluginsData)
    }

    /**
     * Find all resources for plugin.
     */
    protected static List<URL> findPluginResources(WithResources plugin) {
        def path = plugin.class.canonicalName.replace('.', '/')
        def pattern = ~"^$path/.+\$"
        def result = []
        for (def resource : ClassPath.from(plugin.class.classLoader).resources) {
            if (resource.resourceName =~ pattern) {
                result.add(resource.url())
            }
        }
        result
    }

    /**
     * Load all plugins using given {@link PluginLoader} then remove all null plugins
     * and finally inject members to each plugin in case not null injector
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
     * @see WithPriority
     */
    protected static int getPriority(Plugin plugin) {
        plugin instanceof WithPriority ? (plugin as WithPriority).priority : 0
    }

    /**
     * Some checks for plugins.
     * @see AbstractPlugin#isValid(java.lang.Class)
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
}
