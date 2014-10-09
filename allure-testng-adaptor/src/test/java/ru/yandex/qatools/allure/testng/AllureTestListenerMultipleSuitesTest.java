package ru.yandex.qatools.allure.testng;

import org.junit.*;
import org.testng.TestNG;

import com.beust.jcommander.internal.Lists;

import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.model.ObjectFactory;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.allure.utils.AllureResultsUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Validator;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Iterator;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static java.nio.file.FileVisitResult.*;
import static ru.yandex.qatools.allure.commons.AllureFileUtils.listTestSuiteFiles;

public class AllureTestListenerMultipleSuitesTest {

    private static final String SUITE1 = "/suite1.xml";
    private static final String SUITE2 = "/suite2.xml";
    private static final String ALLURE_RESULTS = "allure-results";

    private static Path resultsDir;

    @Before
    public void setUp() throws IOException {
        resultsDir = Files.createTempDirectory(ALLURE_RESULTS);
        AllureResultsUtils.setResultsDirectory(resultsDir.toFile());
        List<String> suites = Lists.newArrayList();
        suites.add(getClass().getResource(SUITE1).getFile());
        suites.add(getClass().getResource(SUITE2).getFile());
        TestNG testNG = new TestNG();
        testNG.setTestSuites(suites);
        testNG.setSuiteThreadPoolSize(2);
        testNG.setUseDefaultListeners(false);
        testNG.run();
    }

    @After
    public void tearDown() throws IOException {
        AllureResultsUtils.setResultsDirectory(null);
        deleteNotEmptyDirectory(resultsDir);
    }

    @Test
    public void suiteFilesCountTest() throws Exception {
        assertThat(listTestSuiteFiles(resultsDir.toFile()).size(), is(2));
    }

    @Test
    public void validateSuiteFilesTest() throws Exception {
        Validator validator = AllureModelUtils.getAllureSchemaValidator();

        for (File each : listTestSuiteFiles(resultsDir.toFile())) {
            validator.validate(new StreamSource(each));
        }
    }
    
    @Test
    public void validateSuiteFilesSameSize() {
    	Iterator<File> iterator = listTestSuiteFiles(resultsDir.toFile()).iterator();
    	File file1 = iterator.next();
    	File file2 = iterator.next();
    	assertThat(file1.length(), is(file2.length()));
    }
    
    @Test 
    public void validatePendingTest() throws JAXBException {
        File resultfile = listTestSuiteFiles(resultsDir.toFile()).iterator().next();
        JAXBContext context = JAXBContext.newInstance(ObjectFactory.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        @SuppressWarnings("unchecked")
        JAXBElement<TestSuiteResult> unmarshalledObject =
                (JAXBElement<TestSuiteResult>) unmarshaller.unmarshal(resultfile);
        TestCaseResult testResult = unmarshalledObject.getValue().getTestCases().get(0);
        
        assertThat(testResult.getStatus(), is(Status.PENDING));
        assertThat(testResult.getDescription().getValue(), is("This is pending test"));  
       
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
