package ru.yandex.qatools.allure.utils;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import org.apache.commons.io.FileUtils;
import ru.yandex.qatools.allure.config.AllureResultsConfig;
import ru.yandex.qatools.allure.exceptions.AllureException;
import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.AttachmentType;
import ru.yandex.qatools.allure.model.ObjectFactory;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;

import static com.google.common.hash.Hashing.sha256;
import static com.google.common.io.Files.hash;
import static org.apache.commons.io.Charsets.UTF_8;
import static ru.yandex.qatools.allure.config.AllureNamingUtils.generateAttachmentFileName;
import static ru.yandex.qatools.allure.config.AllureNamingUtils.generateTestSuiteFileName;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 1/17/14
 */
public class AllureResultsUtils {

    private static File resultsDirectory;

    private static final Object LOCK = new Object();

    private AllureResultsUtils() {
    }

    public static File getResultsDirectory() {
        if (resultsDirectory == null) {
            resultsDirectory = createResultsDirectory();
        }
        return resultsDirectory;
    }

    public static File createResultsDirectory() {
        AllureResultsConfig resultsConfig = new AllureResultsConfig();
        File resultsDirectory = resultsConfig.getResultsDirectory();

        synchronized (LOCK) {
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
            m.setProperty(
                    CharacterEscapeHandler.class.getName(),
                    XmlEscapeHandler.getInstance()
            );
            m.marshal(new ObjectFactory().createTestSuite(testSuiteResult), testSuiteResultFile);
        } catch (JAXBException e) {
            throw new AllureException("Can't marshall test suite result", e);
        }
    }

    public static boolean deleteAttachment(Attachment attachment) {
        File attachmentFile = new File(getResultsDirectory(), attachment.getSource());
        return attachmentFile.exists() && attachmentFile.canWrite() && attachmentFile.delete();
    }

    public static String writeAttachment(Object attachment, AttachmentType type) {
        if (attachment instanceof String) {
            return writeAttachment((String) attachment, type);
        } else if (attachment instanceof File) {
            return writeAttachment((File) attachment, type);
        }
        throw new AllureException("Attach-method should be return 'java.lang.String' or 'java.io.File'.");
    }

    public static String writeAttachment(String attachment, AttachmentType type) {
        try {
            String name = sha256().hashString(attachment, UTF_8).toString();
            String fileName = generateAttachmentFileName(name, type);
            File file = new File(getResultsDirectory(), fileName);
            if (!file.exists()) {
                FileUtils.writeStringToFile(file, attachment, "UTF-8");
            }
            return fileName;
        } catch (IOException e) {
            throw new AllureException("Error while saving attach", e);
        }
    }

    public static String writeAttachment(File attachment, AttachmentType type) {
        try {
            String name = hash(attachment, sha256()).toString();
            String fileName = generateAttachmentFileName(name, type);
            File file = new File(getResultsDirectory(), fileName);
            if (!file.exists()) {
                FileUtils.copyFile(attachment, file);
            }
            return fileName;
        } catch (IOException e) {
            throw new AllureException("Error while saving attach", e);
        }
    }

}
