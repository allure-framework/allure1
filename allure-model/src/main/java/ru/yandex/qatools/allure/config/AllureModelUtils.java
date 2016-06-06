package ru.yandex.qatools.allure.config;

import org.xml.sax.SAXException;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.LabelName;
import ru.yandex.qatools.allure.model.SeverityLevel;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.InputStream;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 20.01.14
 */
public final class AllureModelUtils {

    AllureModelUtils() {
        throw new IllegalStateException("Don't instance AllureModelUtils");
    }

    public static Validator getAllureSchemaValidator() throws SAXException {
        String schemaFileName = AllureConfig.newInstance().getSchemaFileName();
        InputStream schemaFile = ClassLoader.getSystemResourceAsStream(schemaFileName);
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(new StreamSource(schemaFile));
        return schema.newValidator();
    }

    public static Label createFeatureLabel(String feature) {
        return createLabel(LabelName.FEATURE, feature);
    }

    public static Label createStoryLabel(String story) {
        return createLabel(LabelName.STORY, story);
    }

    public static Label createSeverityLabel(SeverityLevel level) {
        return createLabel(LabelName.SEVERITY, level.value());
    }

    public static Label createProgrammingLanguageLabel() {
        return createLabel(LabelName.LANGUAGE, "JAVA");
    }

    public static Label createTestFrameworkLabel(String testFrameworkName) {
        return createLabel(LabelName.FRAMEWORK, testFrameworkName);
    }
    
    public static Label createIssueLabel(String issueKey) {
        return createLabel(LabelName.ISSUE, issueKey);
    }

    public static Label createTestLabel(String testKey) {
        return createLabel(LabelName.TEST_ID, testKey);
    }

    public static Label createHostLabel(String host) {
        return createLabel(LabelName.HOST, host);
    }

    public static Label createThreadLabel(String thread) {
        return createLabel(LabelName.THREAD, thread);
    }

    public static Label createTestSuiteLabel(String testSuite) {
        return createLabel(LabelName.TEST_SUITE, testSuite);
    }

    public static Label createTestGroupLabel(String testGroup) {
        return createLabel(LabelName.TEST_GROUP, testGroup);
    }

    public static Label createTestClassLabel(String testClass) {
        return createLabel(LabelName.TEST_CLASS, testClass);
    }

    public static Label createTestMethodLabel(String testMethod) {
        return createLabel(LabelName.TEST_METHOD, testMethod);
    }

    public static Label createLabel(LabelName name, String value) {
        return new Label().withName(name.value()).withValue(value);
    }
}