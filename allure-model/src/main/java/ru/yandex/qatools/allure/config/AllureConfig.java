package ru.yandex.qatools.allure.config;

import ru.yandex.qatools.properties.PropertyLoader;
import ru.yandex.qatools.properties.annotations.Property;
import ru.yandex.qatools.properties.annotations.Resource;

import java.io.File;
import java.nio.charset.Charset;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/13/13
 */
@SuppressWarnings("unused")
@Resource.Classpath("allure.properties")
public class AllureConfig {

    private static final File DEFAULT_RESULTS_DIRECTORY = new File("target/allure-results");

    @Property("allure.model.schema.file.name")
    private String schemaFileName = "allure.xsd";

    @Property("allure.report.remove.attachments")
    private String removeAttachments = "a^";

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

    @Property("allure.results.environment.xml.file.name")
    private String environmentXmlFileRegex = ".*environment\\.xml";

    @Property("allure.results.environment.properties.file.name")
    private String environmentPropertiesFileRegex = ".*environment\\.properties";

    @Property("allure.results.directory")
    private File resultsDirectory = DEFAULT_RESULTS_DIRECTORY;

    @Property("allure.attachments.encoding")
    private String attachmentsEncoding = "UTF-8";

    private String version = getClass().getPackage().getImplementationVersion();

    public AllureConfig() {
        PropertyLoader.populate(this);
    }

    public String getSchemaFileName() {
        return schemaFileName;
    }

    public String getRemoveAttachments() {
        return removeAttachments;
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

    public String getEnvironmentXmlFileRegex() {
        return environmentXmlFileRegex;
    }

    public String getEnvironmentPropertiesFileRegex() {
        return environmentPropertiesFileRegex;
    }

    public File getResultsDirectory() {
        return resultsDirectory;
    }

    public Charset getAttachmentsEncoding() {
        try {
            return Charset.forName(attachmentsEncoding);
        } catch (Exception ignored) {
            return Charset.defaultCharset();
        }
    }

    public static File getDefaultResultsDirectory() {
        return DEFAULT_RESULTS_DIRECTORY;
    }

    public String getVersion() {
        return version;
    }

    public static AllureConfig newInstance() {
        return new AllureConfig();
    }

}
