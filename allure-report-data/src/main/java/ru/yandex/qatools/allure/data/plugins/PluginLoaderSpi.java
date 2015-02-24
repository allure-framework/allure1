package ru.yandex.qatools.allure.data.plugins;

import com.google.inject.Inject;
import ru.yandex.qatools.allure.data.utils.ServiceLoaderUtils;

import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 17.02.15
 */
public class PluginLoaderSpi implements PluginLoader {

    private ClassLoader classLoader;

    @Inject
    public PluginLoaderSpi(@PluginClassLoader ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public List<Plugin> loadPlugins() {
        return ServiceLoaderUtils.load(classLoader, Plugin.class);
    }
}
