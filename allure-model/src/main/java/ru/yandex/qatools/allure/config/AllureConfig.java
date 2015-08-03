package ru.yandex.qatools.allure.config;

import ru.yandex.qatools.properties.PropertyLoader;
import ru.yandex.qatools.properties.annotations.Property;
import ru.yandex.qatools.properties.annotations.Resource;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/13/13
 */
@SuppressWarnings("unused")
@Resource.Classpath("allure.properties")
public class AllureConfig {

    protected static final File DEFAULT_RESULTS_DIRECTORY = new File("target/allure-results");
    public static final int DEFAULT_MAX_TITLE_LENGTH = 120;

    @Property("allure.model.schema.file.name")
    protected String schemaFileName = "allure.xsd";

    @Property("allure.report.remove.attachments")
    protected String removeAttachments = "a^";

    @Property("allure.results.testsuite.file.regex")
    protected String testSuiteFileRegex = ".*-testsuite\\.xml";

    @Property("allure.results.testsuite.file.suffix")
    protected String testSuiteFileSuffix = "testsuite";

    @Property("allure.results.testsuite.file.extension")
    protected String testSuiteFileExtension = "xml";

    @Property("allure.results.attachment.file.regex")
    protected String attachmentFileRegex = ".+-attachment(\\..+)?";

    @Property("allure.results.attachment.file.suffix")
    protected String attachmentFileSuffix = "-attachment";

    @Property("allure.results.environment.xml.file.name")
    protected String environmentXmlFileRegex = ".*environment\\.xml";

    @Property("allure.results.environment.properties.file.name")
    protected String environmentPropertiesFileRegex = ".*environment\\.properties";

    @Property("allure.results.directory")
    protected File resultsDirectory = DEFAULT_RESULTS_DIRECTORY;

    @Property("allure.attachments.encoding")
    protected Charset attachmentsEncoding = StandardCharsets.UTF_8;

    @Property("allure.max.title.length")
    protected int maxTitleLength = DEFAULT_MAX_TITLE_LENGTH;

    /**
     * Pattern containing issue tracker base URL and one %s placeholder which will be replaced by issue name.
     * Example: http://example.com/%s and @Issue("SOME-123") will give you http://example.com/SOME-123
     */
    @Property("allure.issues.tracker.pattern")
    protected String issueTrackerPattern = "%s";

    /**
     * Pattern containing Test Management System (TMS) and one %s placeholder which will be replaced by test id.
     * Currently there is no special annotation to define TMS test id in your tests. Work in progress...
     * Example: http://example.com/%s and <label name="testId" value="SOME-123"/> will give you http://example.com/SOME-123
     */
    @Property("allure.tests.management.pattern")
    protected String tmsPattern = "%s";

    protected String version = getClass().getPackage().getImplementationVersion();

    public AllureConfig() {
        PropertyLoader.populate(this);
    }

    public static AllureConfig newInstance() { // NOSONAR
        return new AllureConfig();
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
        return attachmentsEncoding;
    }

    public int getMaxTitleLength() {
        return maxTitleLength;
    }

    public String getIssueTrackerPattern() {
        return issueTrackerPattern;
    }

    public String getTmsPattern() {
        return tmsPattern;
    }

    public String getVersion() {
        return version;
    }

    /**
     * For tests only
     */
    public void setResultsDirectory(File resultsDirectory) {
        this.resultsDirectory = resultsDirectory;
    }

    public static File getDefaultResultsDirectory() {
        return DEFAULT_RESULTS_DIRECTORY;
    }
}
