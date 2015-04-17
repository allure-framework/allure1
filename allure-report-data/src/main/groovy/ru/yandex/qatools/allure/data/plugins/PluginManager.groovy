package ru.yandex.qatools.allure.data.plugins

import com.google.inject.Inject
import com.google.inject.Injector
import groovy.transform.stc.ClosureParams
import groovy.transform.stc.FirstParam
import net.sf.corn.cps.CPScanner
import net.sf.corn.cps.ResourceFilter
import org.apache.commons.io.FilenameUtils
import org.apache.commons.io.IOUtils
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
class PluginManager {

    protected final Storage<PreparePlugin> preparePlugins;

    protected final Storage<ProcessPlugin> processPlugins;

    /**
     * Create an instance of plugin manager.
     */
    @Inject
    PluginManager(PluginLoader loader, Injector injector = null) {
        def plugins = load(loader, injector)
        preparePlugins = new Storage<>(filterByType(plugins, PreparePlugin))
        processPlugins = new Storage<>(filterByType(plugins, ProcessPlugin))
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

    void writePluginResources(ReportWriter writer) {
        def plugins = processPlugins.values().flatten()
        def names = [ 'defects', 'xunit', 'behaviors', 'graph', 'timeline']

        plugins.each { plugin ->
            if (plugin.class.isAnnotationPresent(Plugin.Name)) {
                String pluginName = plugin.class.getAnnotation(Plugin.Name).value()
                names.add(pluginName)

                List<URL> resources = findPluginResources(plugin)

                resources.each { resource ->
                    copyResource(writer, pluginName, resource)
                }
            }
        }
        writer.write(new PluginData("plugins.json", names))
    }

    /**
     * Copy given URL resource to writer to plugins/${pluginName} folder
     */
    protected static void copyResource(ReportWriter writer, String pluginName, URL resource) {
        def resourceName = FilenameUtils.getName(resource.toString())
        def input = resource.openStream()

        def output = writer.getPluginResourceOutputStream(pluginName, resourceName)
        output << input
        output.flush()
        output.close()
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
            if (it) {
                injector?.injectMembers(it)
                result.add(it)
            }
        }
        result
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
