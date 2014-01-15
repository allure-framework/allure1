package ru.yandex.qatools.allure.utils;

import org.apache.commons.io.FileUtils;
import ru.yandex.qatools.allure.config.AllureResultsConfig;
import ru.yandex.qatools.allure.exceptions.AllureException;
import ru.yandex.qatools.allure.model.AttachmentType;
import ru.yandex.qatools.allure.model.ObjectFactory;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;

import static ru.yandex.qatools.allure.config.AllureNamingUtils.generateAttachmentFileName;
import static ru.yandex.qatools.allure.config.AllureNamingUtils.generateTestSuiteFileName;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.24.13
 */

public final class AllureWriteUtils {

    private static final String FAQ = "https://github.com/allure-framework/allure-core/allure-core/blob/master/docs/FAQ.md";

    //TODO think about this
    private static final File OUTPUT = new File(AllureResultsConfig.newInstance().getDirectoryPath(), "data");

    private AllureWriteUtils() {
        throw new IllegalStateException();
    }

    public static void copyAttachment(String from, File to) {
        try {
            FileUtils.writeStringToFile(to, from, "UTF-8");
        } catch (IOException e) {
            throw new AllureException("Can't write to file " + to.getAbsolutePath(), e);
        } catch (Exception e) {
            throw new AllureException("Please, see FAQ " + FAQ, e);
        }
    }

    public static void copyAttachment(File from, File to) {
        try {
            FileUtils.copyFile(from, to);
        } catch (IOException e) {
            throw new AllureException("Can't copy attach file", e);
        }
    }

    public static void copyAttachment(Object from, File to) {
        if (from instanceof String) {
            copyAttachment((String) from, to);
        } else if (from instanceof File) {
            copyAttachment((File) from, to);
        } else {
            throw new AllureException("Attach-method should be return 'java.lang.String' or 'java.io.File'.");
        }
    }

    public static String writeAttachment(Object attachment, AttachmentType type) {
        String source = generateAttachmentFileName(type);

        if (OUTPUT.exists() || OUTPUT.mkdirs()) {
            copyAttachment(attachment, new File(OUTPUT, source));
        }
        return source;
    }

    public static void marshal(TestSuiteResult testSuite) {
        marshal(
                OUTPUT,
                generateTestSuiteFileName(),
                new ObjectFactory().createTestSuite(testSuite)
        );
    }

    public static void marshal(final File directory, String name, Object obj) {
        if (directory.exists() || directory.mkdirs()) {
            File testSuiteFile = new File(directory, name);
            JAXB.marshal(obj, testSuiteFile);
        } else {
            throw new AllureException("Can't create " + directory.getName());
        }
    }
}
