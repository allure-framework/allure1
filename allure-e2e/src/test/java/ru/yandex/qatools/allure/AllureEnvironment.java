package ru.yandex.qatools.allure;

import ru.yandex.qatools.properties.PropertyLoader;
import ru.yandex.qatools.properties.annotations.Property;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 1/27/14
 */

public class AllureEnvironment {

    @Property("baseUrl")
    private String baseUrl = "http://0.0.0.0:9010/";

    public AllureEnvironment() {
        PropertyLoader.populate(this);
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public static AllureEnvironment newInstance() {
        return new AllureEnvironment();
    }

}
