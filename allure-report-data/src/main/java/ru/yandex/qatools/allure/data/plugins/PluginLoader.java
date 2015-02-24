package ru.yandex.qatools.allure.data.plugins;

import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 17.02.15
 */
public interface PluginLoader {

    List<Plugin> loadPlugins();
}
