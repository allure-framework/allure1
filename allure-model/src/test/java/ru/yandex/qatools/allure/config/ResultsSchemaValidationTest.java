package ru.yandex.qatools.allure.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FilenameFilter;
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
        Collection<Object[]> testSuiteFileCollection = new ArrayList<>();
        URL url = ResultsSchemaValidationTest.class.getClassLoader().getResource(ALLURE_RESULTS_DIRECTORY_PATH);

        if (url != null) {
            File results = new File(url.getFile());

            for (String testSuiteFilePath : getTestSiteFilesInDirectory(results)) {
                testSuiteFileCollection.add(new Object[]{new File(results, testSuiteFilePath)});
            }
        }
        return testSuiteFileCollection;
    }

    private static String[] getTestSiteFilesInDirectory(File directory) {
        return directory.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches(AllureConfig.newInstance().getTestSuiteFileRegex());
            }
        });
    }

    @Test
    public void testSuiteFileValidationTest() throws Exception {
        Validator validator = AllureModelUtils.getAllureSchemaValidator();
        validator.validate(new StreamSource(testSuiteFile));
    }
}