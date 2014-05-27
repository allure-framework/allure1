package ru.yandex.qatools.allure.config;

import org.apache.commons.io.filefilter.RegexFileFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;


/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/10/13
 */
@RunWith(Parameterized.class)
public class ResultsSchemaValidationTest {

    public static final String ALLURE_RESULTS_DIRECTORY_PATH = "allure-results";

    private final File testSuiteFile;

    public ResultsSchemaValidationTest(File testSuiteFile) {
        this.testSuiteFile = testSuiteFile;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestSuiteFileCollection() {
        AllureConfig resultsConfig = AllureConfig.newInstance();
        Collection<Object[]> testSuiteFileCollection = new ArrayList<>();
        URL url = ClassLoader.getSystemResource(ALLURE_RESULTS_DIRECTORY_PATH);
        if (url != null) {
            File results = new File(url.getFile());

            for (String testSuiteFilePath : results.list(new RegexFileFilter(resultsConfig.getTestSuiteFileRegex()))) {
                testSuiteFileCollection.add(new Object[]{new File(results, testSuiteFilePath)});
            }
        }
        return testSuiteFileCollection;
    }

    @Test
    public void testSuiteFileValidationTest() throws Exception {
        Validator validator = AllureModelUtils.getAllureSchemaValidator();
        validator.validate(new StreamSource(testSuiteFile));
    }
}