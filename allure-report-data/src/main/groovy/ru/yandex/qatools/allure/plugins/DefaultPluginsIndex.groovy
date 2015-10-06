package ru.yandex.qatools.allure.plugins

import static java.util.Collections.unmodifiableList

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.07.15
 */
class DefaultPluginsIndex implements PluginsIndex {

    private final List<Plugin> plugins

    /**
     * Creates an instance of plugin index from given plugins. Plugins were
     * sorted by priority. Plugins with higher priority will be first,
     * if some plugins have equals priority then they will be ordered alphabetically.
     */
    public DefaultPluginsIndex(List<Plugin> plugins) {
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
        filterByType(plugins, type)
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
        return plugins
    }

    /**
     * Get priority of given plugin.
     * @see WithPriority
     */
    protected static int getPriority(Plugin plugin) {
        plugin instanceof WithPriority ? (plugin as WithPriority).priority : 0
    }

    /**
     * Find all plugins with specified type.
     */
    protected static <T> List<T> filterByType(List<? extends Plugin> plugins, Class<T> type) {
        unmodifiableList(plugins.findAll {
            type.isAssignableFrom((it as Object).class)
        } as List<T>)
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
