package ru.yandex.qatools.allure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.LabelName;
import ru.yandex.qatools.allure.model.ObjectFactory;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.nio.file.Files.newDirectoryStream;
import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;
import static javax.xml.bind.Marshaller.JAXB_ENCODING;
import static javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT;
import static ru.yandex.qatools.allure.AllureConstants.ATTACHMENTS_FILE_SUFFIX;
import static ru.yandex.qatools.allure.AllureConstants.TEST_SUITE_FILE_SUFFIX;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 20.01.14
 */
public final class AllureUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllureUtils.class);

    /**
     * Do not instance.
     */
    AllureUtils() {
        throw new IllegalStateException("Don't instance AllureUtils");
    }

    /**
     * Generate suite file name \"{randomUid}-attachment\"
     *
     * @return attachment file name
     */
    public static String generateAttachmentName() {
        return String.format("%s%s", UUID.randomUUID().toString(), ATTACHMENTS_FILE_SUFFIX);
    }

    /**
     * Generate suite file name \"{randomUid}-testsutie.xml\"
     *
     * @return test suite file name
     */
    public static String generateTestSuiteName() {
        return String.format("%s%s.xml", UUID.randomUUID().toString(), TEST_SUITE_FILE_SUFFIX);
    }

    /**
     * Detect bad xml 1.0 characters
     *
     * @param c to detect
     * @return true if specified character valid, false otherwise
     */
    public static boolean isBadXmlCharacter(char c) {
        boolean cDataCharacter = c < '\u0020' && c != '\t' && c != '\r' && c != '\n';
        cDataCharacter |= (c >= '\uD800' && c < '\uE000');
        cDataCharacter |= (c == '\uFFFE' || c == '\uFFFF');
        return cDataCharacter;
    }

    /**
     * Replace bad xml charactes in given array by space
     *
     * @param cbuf buffer to replace in
     * @param off  Offset from which to start reading characters
     * @param len  Number of characters to be replaced
     */
    public static void replaceBadXmlCharactersBySpace(char[] cbuf, int off, int len) {
        for (int i = off; i < off + len; i++) {
            if (isBadXmlCharacter(cbuf[i])) {
                cbuf[i] = '\u0020';
            }
        }
    }

    /**
     * Find all attachment files in specified directories.
     *
     * @see #listFiles(String, Path...)
     */
    public static List<Path> listAttachmentFiles(Path... directories) throws IOException {
        return listFiles(AllureConstants.ATTACHMENTS_FILE_GLOB, directories);
    }

    /**
     * Find all test suite files in specified directories.
     *
     * @see #listFiles(String, Path...)
     */
    public static List<Path> listTestSuiteFiles(Path... directories) throws IOException {
        return listFiles(AllureConstants.TEST_SUITE_FILE_GLOB, directories);
    }

    /**
     * Find all files by glob in specified directories.
     *
     * @param directories the directory to find suite files.
     * @return the list of found test suite files.
     * @throws IOException if any occurs.
     */
    public static List<Path> listFiles(String glob, Path... directories) throws IOException {
        List<Path> result = new ArrayList<>();
        for (Path directory : directories) {
            result.addAll(listFiles(glob, directory));
        }
        return result;
    }

    /**
     * Find all files by glob in specified directory.
     *
     * @param directory the directory to find suite files.
     * @return the list of found test suite files.
     * @throws IOException if any occurs.
     */
    public static List<Path> listFiles(String glob, Path directory) throws IOException {
        List<Path> result = new ArrayList<>();
        if (!Files.isDirectory(directory)) {
            return result;
        }

        try (DirectoryStream<Path> directoryStream = newDirectoryStream(directory, glob)) {
            for (Path path : directoryStream) {
                if (!Files.isDirectory(path)) {
                    result.add(path);
                }
            }
        }
        return result;
    }

    /**
     * Marshal {@link ru.yandex.qatools.allure.model.TestSuiteResult} to specified file
     * uses {@link BadXmlCharacterFilterWriter}. Name of file generated uses
     * {@link #generateTestSuiteName()}
     *
     * @param testSuite to marshal
     */
    public static void marshalTestSuite(TestSuiteResult testSuite, Path file) {
        try (BadXmlCharacterFilterWriter writer = new BadXmlCharacterFilterWriter(file)) {
            marshaller(TestSuiteResult.class).marshal(
                    new ObjectFactory().createTestSuite(testSuite),
                    writer
            );
        } catch (Exception e) {
            LOGGER.error("Error during marshalling testSuite", e);
        }
    }

    /**
     * Unmarshal test suite from given file.
     *
     * @param testSuite to unmarshal.
     * @return the test suite.
     * @throws IOException some occurs during file reading.
     */
    public static TestSuiteResult unmarshalTestSuite(Path testSuite) throws IOException {
        try (BadXmlCharacterFilterReader reader = new BadXmlCharacterFilterReader(testSuite)) {
            return JAXB.unmarshal(reader, TestSuiteResult.class);
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
     * Create an instance of schema validator. Using this validator you can validate
     * your Allure results.
     */
    public static Validator getSchemaValidator() {
        try {
            InputStream schemaFile = AllureUtils.class.getClassLoader()
                    .getResourceAsStream(AllureConstants.SCHEMA_FILE_NAME);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(new StreamSource(schemaFile));
            return schema.newValidator();
        } catch (SAXException e) {
            throw new AllureException("Could not create schema validator", e);
        }
    }

    /**
     * Find and validate all testsuite.xml files in given directories.
     */
    public static void validateResults(Path... directories) {
        try {
            Validator validator = getSchemaValidator();
            for (Path suite : listTestSuiteFiles(directories)) {
                validator.validate(new StreamSource(suite.toFile()));
            }
        } catch (SAXException | IOException e) {
            throw new AllureException("Could not validate results", e);
        }
    }

    /**
     * Create feature label with given label.
     */
    public static Label createFeatureLabel(String feature) {
        return createLabel(LabelName.FEATURE, feature);
    }

    /**
     * Create story label with given label.
     */
    public static Label createStoryLabel(String story) {
        return createLabel(LabelName.STORY, story);
    }

    /**
     * Create severity label with given label.
     */
    public static Label createSeverityLabel(SeverityLevel level) {
        return createLabel(LabelName.SEVERITY, level.value());
    }

    /**
     * Create language label with given label.
     */
    public static Label createProgrammingLanguageLabel() {
        return createLabel(LabelName.LANGUAGE, "JAVA");
    }

    /**
     * Create test framework label with given label.
     */
    public static Label createTestFrameworkLabel(String testFrameworkName) {
        return createLabel(LabelName.FRAMEWORK, testFrameworkName);
    }

    /**
     * Create issue label with given label.
     */
    public static Label createIssueLabel(String issueKey) {
        return createLabel(LabelName.ISSUE, issueKey);
    }

    /**
     * Create test label with given label.
     */
    public static Label createTestLabel(String testKey) {
        return createLabel(LabelName.TEST_ID, testKey);
    }

    /**
     * Create host label with given value.
     */
    public static Label createHostLabel(String host) {
        return createLabel(LabelName.HOST, host);
    }

    /**
     * Create thread label with given value.
     */
    public static Label createThreadLabel(String thread) {
        return createLabel(LabelName.THREAD, thread);
    }

    /**
     * Create label with given {@link LabelName} and value.
     */
    public static Label createLabel(LabelName name, String value) {
        return new Label().withName(name.value()).withValue(value);
    }
}