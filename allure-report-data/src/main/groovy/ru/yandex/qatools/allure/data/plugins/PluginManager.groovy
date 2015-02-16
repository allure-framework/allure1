package ru.yandex.qatools.allure.data.plugins
/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 17.02.15
 */
class PluginManager {

    def storage;

    PluginManager(PluginLoader loader) {
        storage = new Storage(loader)
    }


    public <T> void prepare(T object) {
        def plugins = storage.get(object.class, PreparePlugin)

        for (PreparePlugin<T> plugin : plugins) {
            plugin.prepare(object)
        }
    }

    public <T> void process(T object) {
        def plugins = storage.get(object.class, ProcessPlugin)

        for (ProcessPlugin<T> plugin : plugins) {
            plugin.process(object)
        }
    }

    def getData(Class<?> type) {
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
                put(plugin)
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
