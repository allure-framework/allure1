package ru.yandex.qatools.allure.utils;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import org.apache.commons.io.FileUtils;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import ru.yandex.qatools.allure.config.AllureConfig;
import ru.yandex.qatools.allure.exceptions.AllureException;
import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.ObjectFactory;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;

import static com.google.common.hash.Hashing.sha256;
import static org.apache.commons.io.Charsets.UTF_8;
import static org.apache.tika.mime.MimeTypes.getDefaultMimeTypes;
import static ru.yandex.qatools.allure.config.AllureNamingUtils.generateTestSuiteFileName;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 1/17/14
 */
public class AllureResultsUtils {

    private static File resultsDirectory;

    private static final Object RESULT_DIRECTORY_LOCK = new Object();

    private static final Object ATTACHMENTS_LOCK = new Object();

    private static final AllureConfig CONFIG = AllureConfig.newInstance();

    private AllureResultsUtils() {
    }

    public static File getResultsDirectory() {
        if (resultsDirectory == null) {
            resultsDirectory = createResultsDirectory();
        }
        return resultsDirectory;
    }

    //TODO: allure exception
    public static File createResultsDirectory() {
        File resultsDirectory = CONFIG.getResultsDirectory();

        synchronized (RESULT_DIRECTORY_LOCK) {
            if (resultsDirectory.exists() || resultsDirectory.mkdirs()) {
                return resultsDirectory;
            } else {
                throw new AllureException(
                        String.format("Results directory <%s> doesn't exists or can't be created",
                                resultsDirectory.getAbsolutePath())
                );
            }
        }
    }

    public static void setResultsDirectory(File resultsDirectory) {
        AllureResultsUtils.resultsDirectory = resultsDirectory;
    }

    public static void writeTestSuiteResult(TestSuiteResult testSuiteResult) {
        File testSuiteResultFile = new File(getResultsDirectory(), generateTestSuiteFileName());

        try {
            Marshaller m = JAXBContext.newInstance(TestSuiteResult.class).createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            m.setProperty(
                    CharacterEscapeHandler.class.getName(),
                    XmlEscapeHandler.getInstance()
            );
            m.marshal(new ObjectFactory().createTestSuite(testSuiteResult), testSuiteResultFile);
        } catch (JAXBException ignored) {
            //TODO
        }
    }

    public static boolean deleteAttachment(Attachment attachment) {
        File attachmentFile = new File(getResultsDirectory(), attachment.getSource());
        return attachmentFile.exists() && attachmentFile.canWrite() && attachmentFile.delete();
    }

    public static String writeAttachmentSafety(byte[] attachment, String type) {
        try {
            return writeAttachment(attachment, type);
        } catch (Exception e) {
            return writeAttachmentWithErrorMessage(e);
        }
    }

    public static String writeAttachmentWithErrorMessage(Throwable throwable) {
        String message = throwable.getMessage();
        try {
            writeAttachment(message.getBytes(UTF_8), "test/plain");
        } catch (Exception ignore) {
        }
        return null;
    }

    public static String writeAttachment(byte[] attachment, String type) throws IOException {
        String name = generateAttachmentName(attachment);
        String extension = getExtensionByMimeType(type);
        String source = name + extension;
        File file = new File(getResultsDirectory(), source);
        synchronized (ATTACHMENTS_LOCK) {
            if (!file.exists()) {
                FileUtils.writeByteArrayToFile(file, attachment);
            }
        }
        return source;
    }

    public static String generateAttachmentName(byte[] attachment) {
        String prefix = sha256().hashBytes(attachment).toString();
        return prefix + CONFIG.getAttachmentFileSuffix();
    }

    public static String getExtensionByMimeType(String type) {
        MimeTypes types = getDefaultMimeTypes();
        try {
            return types.forName(type).getExtension();
        } catch (MimeTypeException ignored) {
            return "";
        }
    }
}
