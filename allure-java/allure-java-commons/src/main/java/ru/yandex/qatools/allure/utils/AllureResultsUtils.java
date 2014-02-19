package ru.yandex.qatools.allure.utils;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;
import org.apache.commons.io.FileUtils;
import ru.yandex.qatools.allure.config.AllureResultsConfig;
import ru.yandex.qatools.allure.exceptions.AllureException;
import ru.yandex.qatools.allure.model.AttachmentType;
import ru.yandex.qatools.allure.model.ObjectFactory;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;

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

    public static String writeAttachment(Object attachment, AttachmentType type) {
        String attachmentFileName = generateAttachmentFileName(type);
        File attachmentFile = new File(getResultsDirectory(), attachmentFileName);
        writeAttachment(attachment, attachmentFile);
        return attachmentFileName;
    }

    public static void writeAttachment(Object content, File attachmentFile) {
        if (content instanceof String) {
            writeAttachment((String) content, attachmentFile);
        } else if (content instanceof File) {
            copyAttachment((File) content, attachmentFile);
        } else {
            throw new AllureException("Attach-method should be return 'java.lang.String' or 'java.io.File'.");
        }
    }

    public static void writeAttachment(String content, File attachmentFile) {
        try {
            FileUtils.writeStringToFile(attachmentFile, content, "UTF-8");
        } catch (IOException e) {
            throw new AllureException("Can't write to file " + attachmentFile.getAbsolutePath(), e);
        }
    }

    public static void copyAttachment(File from, File to) {
        try {
            FileUtils.copyFile(from, to);
        } catch (IOException e) {
            throw new AllureException("Can't copy attach file", e);
        }
    }

}
