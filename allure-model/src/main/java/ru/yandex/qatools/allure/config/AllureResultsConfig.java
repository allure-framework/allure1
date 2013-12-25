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
    private String testSuiteFileRegex = ".*-testsuite\\.xml";

    @Property("allure.results.testsuite.file.suffix")
    private String testSuiteFileSuffix = "testsuite";

    @Property("allure.results.testsuite.file.extension")
    private String testSuiteFileExtension = "xml";

    @Property("allure.results.attachment.file.regex")
    private String attachmentFileRegex = ".+-attachment\\.\\w+";

    @Property("allure.results.attachment.file.suffix")
    private String attachmentFileSuffix = "attachment";

    @Property("allure.results.directory.path")
    private String directoryPath = "allure-results";

    public AllureResultsConfig() {
        PropertyLoader.populate(this);
    }

    public String getTestSuiteFileRegex() {
        return testSuiteFileRegex;
    }

    public String getTestSuiteFileSuffix() {
        return testSuiteFileSuffix;
    }

    public String getTestSuiteFileExtension() {
        return testSuiteFileSuffix;
    }

    public String getAttachmentFileRegex() {
        return attachmentFileRegex;
    }

    public String getAttachmentFileSuffix() {
        return attachmentFileSuffix;
    }

    public String getDirectoryPath() {
        return directoryPath;
    }

    public static AllureResultsConfig newInstance() {
        return new AllureResultsConfig();
    }

}
