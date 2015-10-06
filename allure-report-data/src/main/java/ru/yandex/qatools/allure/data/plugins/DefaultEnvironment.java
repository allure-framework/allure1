package ru.yandex.qatools.allure.data.plugins;

import java.util.Map;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 05.10.15
 */
public class DefaultEnvironment implements Environment {

    private final String id;

    private final String name;

    private final String url;

    private final Map<String, String> parameters;

    /**
     * Creates an instance of environment.
     */
    public DefaultEnvironment(String id, String name, String url, Map<String, String> parameters) {
        this.id = id;
        this.name = name != null ? name : "Allure Test Run";
        this.url = url;
        this.parameters = parameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrl() {
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getParameters() {
        return parameters;
    }
}
