package ru.yandex.qatools.allure.utils;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MimeTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.config.AllureConfig;
import ru.yandex.qatools.allure.exceptions.AllureException;
import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.ObjectFactory;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

import static javax.xml.bind.Marshaller.JAXB_ENCODING;
import static javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;
import static org.apache.tika.mime.MimeTypes.getDefaultMimeTypes;
import static ru.yandex.qatools.allure.config.AllureConfig.getDefaultResultsDirectory;
import static ru.yandex.qatools.allure.config.AllureNamingUtils.generateTestSuiteFileName;

/**
 * Using to write test results (test suites and attachments) to result directory
 *
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 1/17/14
 *         <p/>
 */
public final class AllureResultsUtils {

    private static File resultsDirectory;

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
     * and if cannot, returns a new <code>File</code> instance by converting the "allure-results"
     * pathname string into an abstract pathname.
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

        try {
            return Files.createTempDirectory("allure-results").toFile();
        } catch (IOException e) {
            throw new AllureException("Can't create results directory", e);
        }
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
     * Marshal given testSuite to results folder.
     * Shortcut for #writeTestSuiteResult(TestSuiteResult, File)
     */
    public static void writeTestSuiteResult(TestSuiteResult testSuite) {
        writeTestSuiteResult(testSuite, new File(getResultsDirectory(), generateTestSuiteFileName()));
    }

    /**
     * Marshal {@link ru.yandex.qatools.allure.model.TestSuiteResult} to specified file
     * uses {@link BadXmlCharacterFilterWriter}. Name of file generated uses
     * {@link ru.yandex.qatools.allure.config.AllureNamingUtils#generateTestSuiteFileName()}
     *
     * @param testSuite to marshal
     */
    public static void writeTestSuiteResult(TestSuiteResult testSuite, File testSuiteResultFile) {
        try (BadXmlCharacterFilterWriter writer = new BadXmlCharacterFilterWriter(testSuiteResultFile)) {
            marshaller(TestSuiteResult.class).marshal(
                    new ObjectFactory().createTestSuite(testSuite),
                    writer
            );
        } catch (Exception e) {
            LOGGER.error("Error while marshaling testSuite", e);
        }
    }

    /**
     * Creates a new {@link javax.xml.bind.Marshaller} for given class.
     * If marshaller created successfully, try to set following properties:
     * {@link Marshaller#JAXB_FORMATTED_OUTPUT} and {@link Marshaller#JAXB_ENCODING}
     * using {@link #setPropertySafely(javax.xml.bind.Marshaller, String, Object)}
     *
     * @param clazz specified class
     * @return a created marshaller
     * @throws AllureException if can't create marshaller for given class.
     */
    public static Marshaller marshaller(Class<?> clazz) {
        Marshaller m = createMarshallerForClass(clazz);
        setPropertySafely(m, JAXB_FORMATTED_OUTPUT, true);
        setPropertySafely(m, JAXB_ENCODING, StandardCharsets.UTF_8.toString());
        return m;
    }

    /**
     * Creates a new {@link javax.xml.bind.Marshaller} for given class.
     *
     * @param clazz specified class
     * @return a created marshaller
     * @throws AllureException if can't create marshaller for given class.
     */
    public static Marshaller createMarshallerForClass(Class<?> clazz) {
        try {
            return JAXBContext.newInstance(clazz).createMarshaller();
        } catch (JAXBException e) {
            throw new AllureException("Can't create marshaller for class " + clazz, e);
        }
    }

    /**
     * Try to set specified property to given marshaller
     *
     * @param marshaller specified marshaller
     * @param name       name of property to set
     * @param value      value of property to set
     */
    public static void setPropertySafely(Marshaller marshaller, String name, Object value) {
        try {
            marshaller.setProperty(name, value);
        } catch (PropertyException e) {
            LOGGER.warn(String.format("Can't set \"%s\" property to given marshaller", name), e);
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
    public static Attachment writeAttachmentWithErrorMessage(Throwable throwable, String title) {
        String message = throwable.getMessage();
        try {
            return writeAttachment(message.getBytes(CONFIG.getAttachmentsEncoding()), title);
        } catch (Exception e) {
            e.addSuppressed(throwable);
            LOGGER.error(String.format("Can't write attachment \"%s\"", title), e);
        }
        return new Attachment();
    }

    /**
     * Write attachment with specified type. Generate attachment name uses
     * {@link #generateAttachmentName()}, attachment extension uses
     * {@link #getExtensionByMimeType(String)}
     *
     * @param attachment byte array with attachment
     * @param title      attachment title
     * @param type       valid mime-type of attachment
     * @return Created {@link ru.yandex.qatools.allure.model.Attachment}
     * @throws IOException if can't write attachment
     */
    public static Attachment writeAttachment(byte[] attachment, String title, String type) throws IOException {
        String name = generateAttachmentName();
        String extension = getExtensionByMimeType(type);
        String source = name + extension;

        File file = new File(getResultsDirectory(), source);
        synchronized (ATTACHMENTS_LOCK) {
            if (!file.exists()) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(attachment);
                }
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
     * Generate attachment name.
     *
     * @return generated name, looks like \"{uuid}-attachment\"
     */
    public static String generateAttachmentName() {
        return UUID.randomUUID().toString() + CONFIG.getAttachmentFileSuffix();
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
}
