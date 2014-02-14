package ru.yandex.qatools.allure.testng;

import org.junit.*;
import org.testng.TestNG;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.testng.testdata.TestDataClass;

import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static ru.yandex.qatools.allure.config.AllureNamingUtils.listTestSuiteFiles;
import static java.nio.file.FileVisitResult.*;

/**
 * @author Kirill Kozlov kozlov.k.e@gmail.com
 *         Date: 04.02.14
 */
public class AllureTestListenerXmlValidationTest {

    private static final String DEFAULT_SUITE_NAME = "suite";

    private static final String ALLURE_RESULTS_DIRECTORY_PROP = "allure.results.directory";
    private static final String ALLURE_RESULTS = "allure-results";

    private static Path resultsDir;

    @BeforeClass
    public static void setUpClass() throws IOException {
        resultsDir = Files.createTempDirectory(ALLURE_RESULTS);
        System.setProperty(ALLURE_RESULTS_DIRECTORY_PROP, resultsDir.toAbsolutePath().toString());

        AllureTestListener allureListener = new AllureTestListener();
        TestNG testNG = new TestNG();
        testNG.setDefaultSuiteName(DEFAULT_SUITE_NAME);
        testNG.setTestClasses(new Class[] { TestDataClass.class });
        testNG.addListener(allureListener);

        testNG.run();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        System.clearProperty(ALLURE_RESULTS_DIRECTORY_PROP);
        deleteNotEmptyDirectory(resultsDir);
    }

    @Test
    public void suiteFilesCountTest() throws Exception {
        assertThat(listTestSuiteFiles(resultsDir.toFile()).size(), is(1));
    }

    @Test
    public void validateSuiteFilesTest() throws Exception {
        Validator validator = AllureModelUtils.getAllureSchemaValidator();

        for (File each : listTestSuiteFiles(resultsDir.toFile())) {
            validator.validate(new StreamSource(each));
        }
    }

    private static void deleteNotEmptyDirectory(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                if (exc == null) {
                    Files.delete(dir);
                    return CONTINUE;
                } else {
                    throw exc;
                }
            }
        });
    }
}
