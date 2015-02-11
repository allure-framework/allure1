package ru.yandex.qatools.allure.data.plugins;

import java.io.File;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.serialize;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 09.02.15
 */
public abstract class PluginData {

    private String name;

    public PluginData(String name) {
        this.name = name;
    }

    public abstract void setData(Object data);

    public abstract Object getData();

    public int write(File reportDirectory) {
        return serialize(reportDirectory, getName(), getData());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
