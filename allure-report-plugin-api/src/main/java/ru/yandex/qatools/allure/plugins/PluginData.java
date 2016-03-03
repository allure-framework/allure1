package ru.yandex.qatools.allure.plugins;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 09.02.15
 */
public class PluginData {

    private final String name;

    private final Object data;

    public PluginData(String name, Object data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public Object getData() {
        return data;
    }
}
