package ru.yandex.qatools.allure.model;

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
 * Created by eroshenkoam on 12/10/13.
 */
@RunWith(Parameterized.class)
public class ResultsSchemaValidationTest {

    private final static ModelProperties modelProperties = new ModelProperties();

    private final File testSuiteFile;

    private final File schemaFile;

    public ResultsSchemaValidationTest(File testSuiteFile) {
        this.schemaFile = new File(ClassLoader.getSystemResource(modelProperties.getModelFileName()).getFile());
        this.testSuiteFile = testSuiteFile;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestSuiteFileCollection() {
        File results = new File(ClassLoader.getSystemResource(modelProperties.getResultsPath()).getFile());
        Collection testSuiteFileCollection = new ArrayList();
        for (String testSuiteFilePath : results.list()) {
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
