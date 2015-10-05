package ru.yandex.qatools.allure.data.plugins

import com.google.common.hash.Hashing
import com.google.common.reflect.ClassPath
import com.google.inject.Inject
import groovy.transform.CompileStatic
import ru.yandex.qatools.allure.data.Widgets
import ru.yandex.qatools.allure.data.io.ReportWriter
import ru.yandex.qatools.allure.data.utils.PluginUtils

import java.nio.charset.StandardCharsets

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

    protected final List<PreparePlugin> preparePlugins

    protected final List<ProcessPlugin> processPlugins

    protected final List<WithResources> pluginsWithResources

    protected final List<WithWidget> pluginsWithWidgets

    protected final List<WithData> pluginsWithData

    /**
     * Create an instance of plugin manager.
     */
    @Inject
    PluginManager(PluginsIndex index) {
        preparePlugins = index.findAll(PreparePlugin)
        processPlugins = index.findAll(ProcessPlugin)

        pluginsWithResources = index.findAll(WithResources)
        pluginsWithWidgets = index.findAll(WithWidget)
        pluginsWithData = index.findAll(WithData)
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
     * Find all prepare plugins an process given object for
     * each of found plugins.
     */
    public <T> void prepare(T object) {
        def plugins = preparePlugins.findAll { it.type == object?.class }
        plugins*.prepare(object)
    }

    /**
     * Find all process plugins an process given object for
     * each of found plugins.
     */
    public <T> void process(T object) {
        def plugins = processPlugins.findAll { it.type == object?.class }
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
        def Map<String, Object> widgetData = (Map) pluginsWithWidgets.inject([:]) { memo, widget ->
            memo[widget.name] = widget.widgetData
            return memo
        };
        def hash = Hashing.sha1().hashString(widgetData.keySet().join(""), StandardCharsets.UTF_8).toString()
        writer.write(new PluginData(WIDGETS_JSON, new Widgets(hash: hash, plugins: widgetData)))
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
}
