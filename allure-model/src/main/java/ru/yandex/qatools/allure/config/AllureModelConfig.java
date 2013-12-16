package ru.yandex.qatools.allure.config;

import ru.yandex.qatools.properties.PropertyLoader;
import ru.yandex.qatools.properties.annotations.Property;
import ru.yandex.qatools.properties.annotations.Resource;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/13/13
 */
@SuppressWarnings("unused")
@Resource.Classpath("allure.model.properties")
public class AllureModelConfig {

    @Property("allure.model.schema.file.name")
    private String schemaFileName;

    public AllureModelConfig() {
        PropertyLoader.populate(this);
    }

    public String getSchemaFileName() {
        return schemaFileName;
    }

    public static AllureModelConfig newInstance() {
        return new AllureModelConfig();
    }

}
