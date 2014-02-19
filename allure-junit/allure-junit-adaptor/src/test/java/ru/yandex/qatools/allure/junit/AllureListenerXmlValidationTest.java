package ru.yandex.qatools.allure.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.JUnitCore;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.junit.testdata.SimpleTestClass;
import ru.yandex.qatools.allure.utils.AllureResultsUtils;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static ru.yandex.qatools.allure.config.AllureNamingUtils.listTestSuiteFiles;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 20.01.14
 */
public class AllureListenerXmlValidationTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public File resultsDirectory;

    @Before
    public void setUp() throws Exception {
        resultsDirectory = folder.newFolder();
        AllureResultsUtils.setResultsDirectory(resultsDirectory);

        JUnitCore core = new JUnitCore();
        core.addListener(new AllureRunListener());
        core.run(SimpleTestClass.class);
    }

    @Test
    public void suiteFilesCountTest() throws Exception {
        assertThat(listTestSuiteFiles(resultsDirectory).size(), is(1));
    }

    @Test
    public void validateSuiteFilesTest() throws Exception {
        Validator validator = AllureModelUtils.getAllureSchemaValidator();

        for (File each : listTestSuiteFiles(resultsDirectory)) {
            validator.validate(new StreamSource(each));
        }
    }

    @After
    public void tearDown() {
        AllureResultsUtils.setResultsDirectory(null);
    }

}
