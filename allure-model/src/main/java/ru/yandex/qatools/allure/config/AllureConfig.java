package ru.yandex.qatools.allure.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(AllureConfig.class);

    private static final File DEFAULT_RESULTS_DIRECTORY = new File("target/allure-results");
    public static final int DEFAULT_MAX_TITLE_LENGTH = 120;

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

    @Property("allure.attachments.log")
    private boolean attachLogs = false;

    @Property("allure.max.title.length")
    private int maxTitleLength = DEFAULT_MAX_TITLE_LENGTH;

    /**
     * Pattern containing issue tracker base URL and one %s placeholder which will be replaced by issue name.
     * Example: http://example.com/%s and @Issue("SOME-123") will give you http://example.com/SOME-123
     */
    @Property("allure.issues.tracker.pattern")
    private String issueTrackerPattern = "%s";

    /**
     * Pattern containing Test Management System (TMS) and one %s placeholder which will be replaced by test id.
     * Currently there is no special annotation to define TMS test id in your tests. Work in progress...
     * Example: http://example.com/%s and <label name="testId" value="SOME-123"/> will give you http://example.com/SOME-123
     */
    @Property("allure.tests.management.pattern")
    private String tmsPattern = "%s";

    private String version = getClass().getPackage().getImplementationVersion();

    public AllureConfig() {
        PropertyLoader.populate(this);
    }

    public String getSchemaFileName() { // NOSONAR
        return schemaFileName;
    }

    public String getRemoveAttachments() { // NOSONAR
        return removeAttachments;
    }

    public String getTestSuiteFileRegex() { // NOSONAR
        return testSuiteFileRegex;
    }

    public String getTestSuiteFileSuffix() { // NOSONAR
        return testSuiteFileSuffix;
    }

    public String getTestSuiteFileExtension() { // NOSONAR
        return testSuiteFileExtension;
    }

    public String getAttachmentFileRegex() { // NOSONAR
        return attachmentFileRegex;
    }

    public String getAttachmentFileSuffix() { // NOSONAR
        return attachmentFileSuffix;
    }

    public int getMaxTitleLength() { // NOSONAR
        return maxTitleLength;
    }

    public String getEnvironmentXmlFileRegex() { // NOSONAR
        return environmentXmlFileRegex;
    }

    public String getEnvironmentPropertiesFileRegex() { // NOSONAR
        return environmentPropertiesFileRegex;
    }

    public File getResultsDirectory() { // NOSONAR
        return resultsDirectory;
    }

    public Charset getAttachmentsEncoding() { // NOSONAR
        try {
            return Charset.forName(attachmentsEncoding);
        } catch (Exception e) {
            LOGGER.trace("Can't find attachments encoding \"" + attachmentsEncoding, "\" use default", e);
            return Charset.defaultCharset();
        }
    }

    public boolean attachLogs() {
        return attachLogs;
    }

    public static File getDefaultResultsDirectory() { // NOSONAR
        return DEFAULT_RESULTS_DIRECTORY;
    }

    public String getVersion() { // NOSONAR
        return version;
    }

    public String getIssueTrackerPattern() { // NOSONAR
        return issueTrackerPattern;
    }

    public String getTmsPattern() { // NOSONAR
        return tmsPattern;
    }

    public static AllureConfig newInstance() { // NOSONAR
        return new AllureConfig();
    }

}
