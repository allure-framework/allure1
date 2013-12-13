package ru.yandex.qatools.allure.model;

import org.apache.commons.io.filefilter.RegexFileFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;


/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/10/13
 */
@RunWith(Parameterized.class)
public class ResultsSchemaValidationTest {

    private final static AllureModelProperties ALLURE_MODEL_PROPERTIES = new AllureModelProperties();

    private static final String TEST_SUITE_FILES_REGEXP = ".*-testsuite\\.xml";

    private final File testSuiteFile;

    private final File schemaFile;

    public ResultsSchemaValidationTest(File testSuiteFile) {
        this.schemaFile = new File(ClassLoader.getSystemResource(ALLURE_MODEL_PROPERTIES.getModelFileName()).getFile());
        this.testSuiteFile = testSuiteFile;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestSuiteFileCollection() {
        File results = new File(ClassLoader.getSystemResource(ALLURE_MODEL_PROPERTIES.getResultsPath()).getFile());
        Collection<Object[]> testSuiteFileCollection = new ArrayList<>();
        for (String testSuiteFilePath : results.list(new RegexFileFilter(TEST_SUITE_FILES_REGEXP))) {
            testSuiteFileCollection.add(new Object[]{new File(results, testSuiteFilePath)});
        }
        return testSuiteFileCollection;
    }

    @Test
    public void testSuiteFileValidationTest() throws Exception {
        SchemaFactory schemaFactory = SchemaFactory
                .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(schemaFile);
        Validator validator = schema.newValidator();
        validator.validate(new StreamSource(testSuiteFile));
    }
}
