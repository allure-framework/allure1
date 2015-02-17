package ru.yandex.qatools.allure.data.plugins

import com.google.inject.Inject
import ru.yandex.qatools.allure.data.utils.PluginUtils

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 17.02.15
 */
class PluginManager {

    def storage;

    @Inject
    PluginManager(PluginLoader loader) {
        storage = new Storage(loader)
    }


    public <T> void prepare(T object) {
        if (!object) {
            return
        }
        def plugins = storage.get(object.class, PreparePlugin)

        for (PreparePlugin<T> plugin : plugins) {
            plugin.prepare(object)
        }
    }

    public <T> void process(T object) {
        if (!object) {
            return
        }
        def plugins = storage.get(object.class, ProcessPlugin)

        for (ProcessPlugin<T> plugin : plugins) {
            plugin.process(PluginUtils.clone(object))
        }
    }

    List<PluginData> getData(Class<?> type) {
        if (!type) {
            []
        }
        storage.get(type, ProcessPlugin).collect {
            it.pluginData
        }.flatten()
    }

    class Storage {

        private Map<Class, Map<Class, List<Plugin>>> storage = new HashMap<>().withDefault {
            new HashMap<>().withDefault {
                new ArrayList<>()
            }
        };

        Storage(PluginLoader loader) {
            for (Plugin plugin : loader.loadPlugins()) {
                if (plugin) {
                    put(plugin)
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
