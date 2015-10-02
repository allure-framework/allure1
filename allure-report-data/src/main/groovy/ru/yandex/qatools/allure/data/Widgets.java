package ru.yandex.qatools.allure.data;

import java.util.Map;

public class Widgets {

    protected String hash;
    protected Map<String, Object> plugins;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Map<String, Object> getPlugins() {
        return plugins;
    }

    public void setPlugins(Map<String, Object> plugins) {
        this.plugins = plugins;
    }
}
