package ru.yandex.qatools.allure.model;

import ru.yandex.qatools.properties.PropertyLoader;
import ru.yandex.qatools.properties.annotations.Property;
import ru.yandex.qatools.properties.annotations.Resource;

/**
 * User: eroshenkoam
 * Date: 11/6/13, 7:11 PM
 */

@SuppressWarnings("unused")
@Resource.Classpath("model.properties")
public class ModelProperties {

    @Property("allure.model.results.path")
    private String resultsPath;

    @Property("allure.model.file.name")
    private String modelFileName;

    public ModelProperties() {
        PropertyLoader.populate(this);
    }

    public String getResultsPath() {
        return resultsPath;
    }

    public String getModelFileName() {
        return modelFileName;
    }
}
