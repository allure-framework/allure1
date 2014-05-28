package ru.yandex.qatools.allure.utils;

import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.config.AllureConfig;
import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.ObjectFactory;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import static com.google.common.hash.Hashing.sha256;
import static javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;
import static org.apache.commons.io.Charsets.UTF_8;
import static org.apache.tika.mime.MimeTypes.getDefaultMimeTypes;
import static ru.yandex.qatools.allure.config.AllureConfig.getDefaultResultsDirectory;
import static ru.yandex.qatools.allure.config.AllureNamingUtils.generateTestSuiteFileName;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 1/17/14
 *         <p/>
 *         Using to write test results (test suites and attachments) to result directory
 */
public final class AllureResultsUtils {

    private static File resultsDirectory;

    private static final String ESCAPE_HANDLER_PROPERTY = "com.sun.xml.bind.marshaller.CharacterEscapeHandler";

    private static final Object RESULT_DIRECTORY_LOCK = new Object();

    private static final Object ATTACHMENTS_LOCK = new Object();

    private static final AllureConfig CONFIG = AllureConfig.newInstance();

    private static final Logger LOGGER = LoggerFactory.getLogger(AllureResultsUtils.class);

    /**
     * Don't use instance
     */
    AllureResultsUtils() {
        throw new IllegalStateException("Don't instance AllureResultsUtils");
    }

    /**
     * Returns results directory. If {@link #resultsDirectory} not set create new
     * directory uses {@link #createResultsDirectory()} method.
     *
     * @return results directory
     */
    public static File getResultsDirectory() {
        synchronized (RESULT_DIRECTORY_LOCK) {
            if (resultsDirectory == null) {
                resultsDirectory = createResultsDirectory();
            }
        }
        return resultsDirectory;
    }

    /**
     * Create results directory. First step try to create {@link #CONFIG#getResultsDirectory()},
     * if cannot, try to create {@link ru.yandex.qatools.allure.config.AllureConfig#getDefaultResultsDirectory},
     * and if cannot, returns {@link com.google.common.io.Files#createTempDir()}
     *
     * @return created results directory
     */
    private static File createResultsDirectory() {
        File resultsDirectory = CONFIG.getResultsDirectory();

        if (createDirectories(resultsDirectory)) {
            return resultsDirectory;
        }

        LOGGER.error(String.format(
                "Results directory <%s> doesn't exists and can't be created, using default directory",
                resultsDirectory.getAbsolutePath()
        ));

        resultsDirectory = getDefaultResultsDirectory();
        if (createDirectories(resultsDirectory)) {
            return resultsDirectory;
        }

        LOGGER.error(String.format(
                "Default results directory <%s> doesn't exists and can't be created, using temp directory",
                resultsDirectory.getAbsolutePath()
        ));

        return Files.createTempDir();
    }

    /**
     * Try to create the directory named by this abstract pathname, including any
     * necessary but nonexistent parent directories.
     *
     * @param dir to create
     * @return true, if directory successfully created, false otherwise
     */
    public static boolean createDirectories(File dir) {
        return dir.exists() || dir.mkdirs();
    }

    /**
     * Sets results directory. Use only for tests
     *
     * @param resultsDirectory results directory to set
     */
    public static void setResultsDirectory(File resultsDirectory) {
        AllureResultsUtils.resultsDirectory = resultsDirectory;
    }

    /**
     * Marshal {@link ru.yandex.qatools.allure.model.TestSuiteResult} to {@link #resultsDirectory}
     * uses {@link BadXmlCharacterEscapeHandler}. Name of file generated uses
     * {@link ru.yandex.qatools.allure.config.AllureNamingUtils#generateTestSuiteFileName()}
     *
     * @param testSuite to marshal
     */
    public static void writeTestSuiteResult(TestSuiteResult testSuite) {
        File testSuiteResultFile = new File(getResultsDirectory(), generateTestSuiteFileName());

        try {
            Marshaller m = JAXBContext.newInstance(TestSuiteResult.class).createMarshaller();
            m.setProperty(JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(ESCAPE_HANDLER_PROPERTY, BadXmlCharacterEscapeHandler.getInstance());

            m.marshal(new ObjectFactory().createTestSuite(testSuite), testSuiteResultFile);
        } catch (Exception e) {
            LOGGER.error("Error while marshaling testSuite", e);
        }
    }

    /**
     * Remove attachment form {@link #resultsDirectory}
     *
     * @param attachment to remove
     * @return true, if attachment removed successfully, false otherwise
     */
    public static boolean deleteAttachment(Attachment attachment) {
        File attachmentFile = new File(getResultsDirectory(), attachment.getSource());
        return attachmentFile.exists() && attachmentFile.canWrite() && attachmentFile.delete();
    }

    /**
     * Write attachment uses {@link #writeAttachment(byte[], String, String)} (if
     * specified attachment type not empty) or {@link #writeAttachment(byte[], String)}
     * otherwise. If something went wrong uses
     * {@link #writeAttachmentWithErrorMessage(Throwable, String)}
     *
     * @param attachment which will write
     * @param title      attachment title
     * @param type       attachment type (should be valid mime-type, empty string or null)
     * @return Created {@link ru.yandex.qatools.allure.model.Attachment}
     */
    public static Attachment writeAttachmentSafely(byte[] attachment, String title, String type) {
        try {
            return type == null || type.isEmpty()
                    ? writeAttachment(attachment, title)
                    : writeAttachment(attachment, title, type);

        } catch (Exception e) {
            return writeAttachmentWithErrorMessage(e, title);
        }
    }

    /**
     * Write throwable as attachment.
     *
     * @param throwable to write
     * @param title     title of attachment
     * @return Created {@link ru.yandex.qatools.allure.model.Attachment}
     */
    public static Attachment writeAttachmentWithErrorMessage(Throwable throwable, String title) {
        String message = throwable.getMessage();
        try {
            return writeAttachment(message.getBytes(UTF_8), title);
        } catch (Exception e) {
            e.addSuppressed(throwable);
            LOGGER.error(String.format("Can't write attachment \"%s\"", title), e);
        }
        return new Attachment();
    }

    /**
     * Write attachment with specified type. Generate attachment name uses
     * {@link #generateAttachmentName(byte[])}, attachment extension uses
     * {@link #getExtensionByMimeType(String)}
     *
     * @param attachment byte array with attachment
     * @param title      attachment title
     * @param type       valid mime-type of attachment
     * @return Created {@link ru.yandex.qatools.allure.model.Attachment}
     * @throws IOException if can't write attachment
     */
    public static Attachment writeAttachment(byte[] attachment, String title, String type) throws IOException {
        String name = generateAttachmentName(attachment);
        String extension = getExtensionByMimeType(type);
        String source = name + extension;

        File file = new File(getResultsDirectory(), source);
        synchronized (ATTACHMENTS_LOCK) {
            if (!file.exists()) {
                FileUtils.writeByteArrayToFile(file, attachment);
            }
        }
        return new Attachment().withTitle(title).withSource(source).withType(type);
    }

    /**
     * Write attachment without specified type. Using {@link org.apache.tika.mime.MimeTypes#detect}
     * to autodetect attachment type from attachment content.
     *
     * @param attachment to write
     * @param title      attachment title
     * @return Created {@link ru.yandex.qatools.allure.model.Attachment}
     * @throws IOException if can't write attachment
     */
    public static Attachment writeAttachment(byte[] attachment, String title) throws IOException {
        String type = getDefaultMimeTypes().detect(new ByteArrayInputStream(attachment), new Metadata()).toString();
        return writeAttachment(attachment, title, type);
    }

    /**
     * Generate attachment name as sha256 of attachment content
     *
     * @param attachment content
     * @return generated name, looks like \"{sha256}-attachment\"
     */
    public static String generateAttachmentName(byte[] attachment) {
        String prefix = sha256().hashBytes(attachment).toString();
        return prefix + CONFIG.getAttachmentFileSuffix();
    }

    /**
     * Generate attachment extension from mime type
     *
     * @param type valid mime-type
     * @return extension if it's known for specified mime-type, or empty string
     * otherwise
     */
    public static String getExtensionByMimeType(String type) {
        MimeTypes types = getDefaultMimeTypes();
        try {
            return types.forName(type).getExtension();
        } catch (Exception e) {
            LOGGER.warn("Can't detect extension for MIME-type", e);
            return "";
        }
    }
}
