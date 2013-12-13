package ru.yandex.qatools.allure.config;

import ru.yandex.qatools.properties.PropertyLoader;
import ru.yandex.qatools.properties.annotations.Property;
import ru.yandex.qatools.properties.annotations.Resource;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/13/13
 */
@SuppressWarnings("unused")
@Resource.Classpath("allure.results.properties")
public class AllureResultsConfig {

    @Property("allure.results.testsuite.file.regex")
    private String testSuiteFileRegex;

    @Property("allure.results.directory.path")
    private String directoryPath;

    public AllureResultsConfig() {
        PropertyLoader.populate(this);
    }

    public String getTestSuiteFileRegex() {
        return testSuiteFileRegex;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public static AllureResultsConfig newInstance() {
        return new AllureResultsConfig();
    }

}
