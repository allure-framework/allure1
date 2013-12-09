package ru.yandex.qatools.allure.utils;

import org.apache.commons.io.FileUtils;
import ru.yandex.qatools.allure.exceptions.AllureException;
import ru.yandex.qatools.allure.model.AttachmentType;
import ru.yandex.qatools.allure.model.ObjectFactory;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.24.13
 */

public final class AllureWriteUtils {

    private static final String FAQ = "https://github.com/allure-framework/allure-core/allure-core/blob/master/docs/FAQ.md";

    private static final String XML = ".xml";

    private static final String TEST_SUITE = "-testsuite";

    private static final String ATTACHMENT = "-attachment";

    private static final File OUTPUT = new File("target/site/allure-maven-plugin/data");

    private AllureWriteUtils() {
        throw new IllegalStateException();
    }

    public static String humanize(String text) {
        String result = text.trim();
        result = splitCamelCase(result);
        result = result.replaceAll("(_)+", " ");
        result = underscoreCapFirstWords(result);
        result = capitalize(result);

        return result;
    }

    public static String capitalize(String text) {
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

    public static String underscoreCapFirstWords(String text) {
        Matcher matcher = Pattern.compile("(^|\\w|\\s)([A-Z]+)([a-z]+)").matcher(text);

        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, matcher.group().toLowerCase());
        }
        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }

    public static String splitCamelCase(String camelCaseString) {
        return camelCaseString.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[a-z0-9])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[0-9])"
                ),
                "_"
        );
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

    public static String writeAttachment(Object attachment, AttachmentType type, String defaultSuffix) {
        String suffix = type != AttachmentType.OTHER ? "." + type.toString().toLowerCase() : defaultSuffix;

        String attachmentUid = UUID.randomUUID().toString();
        String source = attachmentUid + ATTACHMENT + suffix;

        if (OUTPUT.exists() || OUTPUT.mkdirs()) {
            copyAttachment(attachment, new File(OUTPUT, source));
        }
        return source;
    }

    public static void marshalTestSuite(TestSuiteResult testSuite) {
        marshalFile(
                OUTPUT,
                UUID.randomUUID().toString() + TEST_SUITE + XML,
                new ObjectFactory().createTestSuite(testSuite)
        );
    }

    public static void marshalFile(final File directory, String name, Object obj) {
        if (directory.exists() || directory.mkdirs()) {
            File testSuiteFile = new File(directory, name);
            JAXB.marshal(obj, testSuiteFile);
        } else {
            throw new AllureException("Can't create " + directory.getName());
        }
    }
}
