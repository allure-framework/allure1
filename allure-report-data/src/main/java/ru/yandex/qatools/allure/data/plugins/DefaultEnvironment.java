package ru.yandex.qatools.allure.data.plugins;

import com.google.common.collect.Maps;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 05.10.15
 */
public class DefaultEnvironment implements Environment {

    public static final String ALLURE_TEST_RUN_NAME = "allure.test.run.name";

    public static final String ALLURE_TEST_RUN_ID = "allure.test.run.id";

    public static final String ALLURE_TEST_RUN_URL = "allure.test.run.url";

    private final String id;

    private final String name;

    private final String url;

    private final Map<String, String> environment;

    public DefaultEnvironment(Properties properties) {
        this.id = properties.getProperty(ALLURE_TEST_RUN_ID);
        this.name = properties.getProperty(ALLURE_TEST_RUN_NAME, "Allure Test Run");
        this.url = properties.getProperty(ALLURE_TEST_RUN_URL);

        Map<String, String> map = new HashMap<>(Maps.fromProperties(properties));
        map.remove(ALLURE_TEST_RUN_ID);
        map.remove(ALLURE_TEST_RUN_NAME);
        map.remove(ALLURE_TEST_RUN_URL);
        this.environment = Collections.unmodifiableMap(map);
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
        return environment;
    }
}
