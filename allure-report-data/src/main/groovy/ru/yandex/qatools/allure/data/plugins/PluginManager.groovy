package ru.yandex.qatools.allure.data.plugins

import com.google.inject.Inject
import ru.yandex.qatools.allure.data.io.ReportWriter
import ru.yandex.qatools.allure.data.utils.PluginUtils

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 17.02.15
 */
class PluginManager {

    Storage storage;

    @Inject
    PluginManager(PluginLoader loader) {
        storage = new Storage(loader)
    }

    public <T> void prepare(T object) {
        if (!object) {
            return
        }
        def plugins = storage.get(object.class, PreparePlugin)

        plugins.each {
            it.prepare(object)
        }
    }

    public <T> void process(T object) {
        if (!object) {
            return
        }
        def plugins = storage.get(object.class, ProcessPlugin)

        plugins.each {
            it.process(PluginUtils.clone(object))
        }
    }

    public <T> void writePluginData(Class<T> type, ReportWriter writer) {
        writer.write(getData(type))
    }


    protected List<PluginData> getData(Class<?> type) {
        if (!type) {
            []
        }
        storage.get(type, ProcessPlugin).collect {
            it.pluginData
        }.flatten()
    }

    protected class Storage {

        private Map<Class, Map<Class, List<Plugin>>> storage = new HashMap<>().withDefault {
            new HashMap<>().withDefault {
                new ArrayList<>()
            }
        };

        Storage(PluginLoader loader) {
            def plugins = loader.loadPlugins()
            if (!plugins) {
                return
            }

            plugins.each {
                if (it) {
                    put(it)
                }
            }
        }

        public <T, S extends Plugin<T>> List<S> get(Class<T> objectType, Class<S> pluginType) {
            storage[objectType]?.get(pluginType) as List<S> ?: []
        }

        public void put(Plugin plugin) {
            def type = plugin.type
            def classes = [ProcessPlugin, PreparePlugin]
            classes.each {
                if (it.isAssignableFrom(plugin.class)) {
                    storage[type][it].add(plugin)
                }
            }
        }
    }
}
