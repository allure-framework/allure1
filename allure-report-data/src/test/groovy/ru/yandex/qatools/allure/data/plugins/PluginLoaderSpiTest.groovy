package ru.yandex.qatools.allure.data.plugins

import org.junit.Before
import org.junit.Test

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 07.02.15
 */
class PluginLoaderSpiTest {

    PluginLoader pluginLoader

    @Before
    void setUp() {
        URL url = getClass().classLoader.getResource("testdata/")
        URLClassLoader classLoader = new URLClassLoader(
                [url] as URL[],
                Thread.currentThread().contextClassLoader
        )
        pluginLoader = new PluginLoaderSpi(classLoader)
    }

    @Test
    void shouldLoadPlugins() {
        def plugins = pluginLoader.loadPlugins()
        assert plugins
        assert plugins.collect { it.class.canonicalName }.containsAll([
                "ru.yandex.qatools.allure.data.testdata.SomePlugin",
                "ru.yandex.qatools.allure.data.testdata.SomePreparePlugin",
                "ru.yandex.qatools.allure.data.testdata.SomeProcessPlugin"
        ])
    }
}
