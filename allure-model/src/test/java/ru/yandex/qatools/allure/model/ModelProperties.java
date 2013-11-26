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

    @Property("results.path")
    private String resultsPath;

    public ModelProperties() {
        PropertyLoader.populate(this);
    }

    public String getResultsPath() {
        return resultsPath;
    }
}
