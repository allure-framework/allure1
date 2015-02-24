package ru.yandex.qatools.allure.data.plugins;

import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.02.15
 */
public interface ProcessPlugin<T> extends Plugin<T> {

    void process(T data);

    List<PluginData> getPluginData();
}
