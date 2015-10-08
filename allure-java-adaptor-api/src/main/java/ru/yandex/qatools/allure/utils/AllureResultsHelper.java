package ru.yandex.qatools.allure.utils;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.AllureConfig;
import ru.yandex.qatools.allure.AllureException;
import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.Step;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.regex.Pattern;

import static org.apache.tika.mime.MimeTypes.getDefaultMimeTypes;
import static ru.yandex.qatools.allure.AllureUtils.generateAttachmentName;
import static ru.yandex.qatools.allure.AllureUtils.generateTestSuiteXmlName;
import static ru.yandex.qatools.allure.AllureUtils.marshalTestSuite;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 30.09.15
 */
public class AllureResultsHelper {

    private static final Object ATTACHMENTS_LOCK = new Object();

    private static final Logger LOGGER = LoggerFactory.getLogger(AllureResultsHelper.class);

    private final AllureConfig config;

    private final Path resultsDirectory;

    /**
     * Creates an instance of helper.
     */
    public AllureResultsHelper(AllureConfig config) {
        this.config = config;
        this.resultsDirectory = createResultsDirectory(config);
    }

    /**
     * Remove attachments matches pattern from step and all substeps.
     */
    public void deleteAttachments(Step step, Pattern pattern) {
        Iterator<Attachment> iterator = step.getAttachments().listIterator();
        while (iterator.hasNext()) {
            Attachment attachment = iterator.next();
            if (pattern.matcher(attachment.getSource()).matches()) {
                deleteAttachment(attachment);
                iterator.remove();
            }
        }

        for (Step child : step.getSteps()) {
            deleteAttachments(child, pattern);
        }
    }

    /**
     * Remove attachment form {@link #resultsDirectory}
     *
     * @param attachment to remove
     * @return true, if attachment removed successfully, false otherwise
     */
    public boolean deleteAttachment(Attachment attachment) {
        try {
            return Files.deleteIfExists(resultsDirectory.resolve(attachment.getSource()));
        } catch (IOException e) {
            LOGGER.error("Could not delete attachment file " + attachment.getSource(), e);
            return false;
        }
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
    public Attachment writeAttachmentSafely(byte[] attachment, String title, String type) {
        try {
            return type == null || type.isEmpty()
                    ? writeAttachment(attachment, title)
                    : writeAttachment(attachment, title, type);
        } catch (Exception e) {
            LOGGER.trace("Error while saving attachment " + title + ":" + type, e);
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
    public Attachment writeAttachmentWithErrorMessage(Throwable throwable, String title) {
        String message = throwable.getMessage();
        try {
            return writeAttachment(message.getBytes(config.getAttachmentsEncoding()), title);
        } catch (Exception e) {
            e.addSuppressed(throwable);
            LOGGER.error(String.format("Can't write attachment \"%s\"", title), e);
        }
        return new Attachment();
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
    public Attachment writeAttachment(byte[] attachment, String title) throws IOException {
        String type = getDefaultMimeTypes().detect(new ByteArrayInputStream(attachment), new Metadata()).toString();
        return writeAttachment(attachment, title, type);
    }

    /**
     * Write attachment with specified type. Generate attachment name uses
     * {@link ru.yandex.qatools.allure.AllureUtils#generateAttachmentName()}, attachment extension uses
     * {@link #getExtensionByMimeType(String)}
     *
     * @param attachment byte array with attachment
     * @param title      attachment title
     * @param type       valid mime-type of attachment
     * @return Created {@link ru.yandex.qatools.allure.model.Attachment}
     * @throws IOException if can't write attachment
     */
    public Attachment writeAttachment(byte[] attachment, String title, String type) throws IOException {
        String name = generateAttachmentName();
        String extension = getExtensionByMimeType(type);
        String source = name + extension;

        Path file = resultsDirectory.resolve(source);
        synchronized (ATTACHMENTS_LOCK) {
            if (Files.notExists(file)) {
                Files.write(file, attachment);
            }
        }
        return new Attachment().withTitle(title).withSource(source).withType(type);
    }

    /**
     * Marshal given testSuite to results folder.
     * Shortcut for #writeTestSuiteResult(TestSuiteResult, File)
     */
    public void writeTestSuite(TestSuiteResult testSuite) {
        marshalTestSuite(testSuite, resultsDirectory.resolve(generateTestSuiteXmlName()));
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
            LOGGER.warn("Can't detect extension for MIME-type " + type, e);
            return "";
        }
    }

    /**
     * Create results directory.
     *
     * @return created results directory
     */
    private static synchronized Path createResultsDirectory(AllureConfig config) {
        Path resultsDirectory = config.getResultsDirectory();
        try {
            return Files.createDirectories(resultsDirectory);
        } catch (IOException e) {
            throw new AllureException("Can't create results directory " + resultsDirectory, e);
        }
    }
}
