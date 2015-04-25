package ru.yandex.qatools.allure.data.plugins;

import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.15
 * @see AbstractPlugin
 */
public interface WithData {

    /**
     * Using this method plugin can store some information to
     * data folder.
     */
    List<PluginData> getPluginData();
}
