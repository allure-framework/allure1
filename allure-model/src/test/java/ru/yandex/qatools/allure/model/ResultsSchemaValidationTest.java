package ru.yandex.qatools.allure.model;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.apache.commons.io.filefilter.RegexFileFilter;
import org.junit.runners.Parameterized;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.config.AllureResultsConfig;

import javax.xml.transform.stream.StreamSource;
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

    public static final String ALLURE_RESULTS_DIRECTORY_PATH = "allure-results";

    private final File testSuiteFile;

    public ResultsSchemaValidationTest(File testSuiteFile) {
        this.testSuiteFile = testSuiteFile;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestSuiteFileCollection() {
        AllureResultsConfig resultsConfig = AllureResultsConfig.newInstance();
        File results = new File(ClassLoader.getSystemResource(ALLURE_RESULTS_DIRECTORY_PATH).getFile());
        Collection<Object[]> testSuiteFileCollection = new ArrayList<>();
        for (String testSuiteFilePath : results.list(new RegexFileFilter(resultsConfig.getTestSuiteFileRegex()))) {
            testSuiteFileCollection.add(new Object[]{new File(results, testSuiteFilePath)});
        }
        return testSuiteFileCollection;
    }

    @Test
    public void testSuiteFileValidationTest() throws Exception {
        Validator validator = AllureModelUtils.getAllureSchemaValidator();
        validator.validate(new StreamSource(testSuiteFile));
    }
}
