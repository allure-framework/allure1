package ru.yandex.qatools.allure.junit;

import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.JUnitCore;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.junit.testdata.SimpleTestClass;

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

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    public static File resultsDirectory;

    @BeforeClass
    public static void setUp() throws Exception {
        resultsDirectory = folder.newFolder();
        System.setProperty("allure.results.directory", resultsDirectory.getAbsolutePath());

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

    @AfterClass
    public static void tearDown() {
        System.setProperty("allure.results.directory", "");
    }

}
