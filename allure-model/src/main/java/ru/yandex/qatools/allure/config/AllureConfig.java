package ru.yandex.qatools.allure.config;

import ru.yandex.qatools.properties.PropertyLoader;
import ru.yandex.qatools.properties.annotations.Property;
import ru.yandex.qatools.properties.annotations.Resource;

import java.io.File;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/13/13
 */
@SuppressWarnings("unused")
@Resource.Classpath("allure.properties")
public class AllureConfig {

    private static final String VERSION_FILE_NAME = "version.txt";

    /**
     * Allure.getVersion() added in version 1.3.6
     */
    private static final String DEFAULT_VERSION = "1.3.6";

    @Property("allure.model.schema.file.name")
    private String schemaFileName = "allure.xsd";

    @Property("allure.report.remove.attachments")
    private String removeAttachments = "a^";

    @Property("allure.report.remove.passed.tests")
    private boolean removePassedTests = false;

    @Property("allure.results.testsuite.file.regex")
    private String testSuiteFileRegex = ".*-testsuite\\.xml";

    @Property("allure.results.testsuite.file.suffix")
    private String testSuiteFileSuffix = "testsuite";

    @Property("allure.results.testsuite.file.extension")
    private String testSuiteFileExtension = "xml";

    @Property("allure.results.attachment.file.regex")
    private String attachmentFileRegex = ".+-attachment(\\..+)?";

    @Property("allure.results.attachment.file.suffix")
    private String attachmentFileSuffix = "-attachment";

    @Property("allure.results.directory")
    private File resultsDirectory = new File("target/allure-results");

    private String version = null;

    public AllureConfig() {
        PropertyLoader.populate(this);
    }

    public String getSchemaFileName() {
        return schemaFileName;
    }

    public String getRemoveAttachments() {
        return removeAttachments;
    }

    public boolean isRemovePassedTests() {
        return removePassedTests;
    }

    public String getTestSuiteFileRegex() {
        return testSuiteFileRegex;
    }

    public String getTestSuiteFileSuffix() {
        return testSuiteFileSuffix;
    }

    public String getTestSuiteFileExtension() {
        return testSuiteFileExtension;
    }

    public String getAttachmentFileRegex() {
        return attachmentFileRegex;
    }

    public String getAttachmentFileSuffix() {
        return attachmentFileSuffix;
    }

    public File getResultsDirectory() {
        return resultsDirectory;
    }

    public String getVersion() {
        return getClass().getPackage().getImplementationVersion();
    }

    public static AllureConfig newInstance() {
        return new AllureConfig();
    }

}
