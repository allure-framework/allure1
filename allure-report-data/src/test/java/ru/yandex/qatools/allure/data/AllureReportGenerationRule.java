package ru.yandex.qatools.allure.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.rules.ExternalResource;
import ru.yandex.qatools.allure.commons.AllureFileUtils;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static ru.yandex.qatools.allure.commons.AllureFileUtils.listTestSuiteFiles;


/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/10/13
 */
public class AllureReportGenerationRule extends ExternalResource {

    private boolean deleteResultsDirectory = false;

    private final File reportDir;

    private final File resultsDir;

    private AllureXUnit allureXUnit;

    private AllureDefects allureDefects;

    private List<AllureTestCase> allureTestCases;

    private List<TestSuiteResult> testSuiteResults;

    public AllureReportGenerationRule(String... resourceNames) {
        this(FileUtils.getTempDirectory(), createResultsDirectoryFromResources(resourceNames));
        deleteResultsDirectory = true;
    }

    public AllureReportGenerationRule(File resultsDirectory) {
        this(FileUtils.getTempDirectory(), resultsDirectory);
    }

    public AllureReportGenerationRule(File reportDir, File resultsDir) {
        this.reportDir = reportDir;
        this.resultsDir = resultsDir;
    }


    protected void before() throws Throwable {
        AllureReportGenerator reportGenerator = new AllureReportGenerator(resultsDir);
        reportGenerator.generate(reportDir);
    }

    protected void after() {
        FileUtils.deleteQuietly(this.reportDir);
        if (deleteResultsDirectory) {
            FileUtils.deleteQuietly(this.resultsDir);
        }
    }

    public AllureXUnit getXUnitData() throws Exception {
        if (allureXUnit == null) {
            ObjectMapper mapper = new ObjectMapper();
            allureXUnit = mapper.readValue(new File(reportDir, "data/xunit.json"), AllureXUnit.class);
        }
        return allureXUnit;
    }

    public AllureDefects getDefectsData() throws Exception {
        if (allureDefects == null) {
            ObjectMapper mapper = new ObjectMapper();
            allureDefects = mapper.readValue(new File(reportDir, "data/defects.json"), AllureDefects.class);
        }
        return allureDefects;
    }

    public List<AllureTestCase> getTestCasesData() throws Exception {
        if (allureTestCases == null) {
            allureTestCases = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();
            for (File testCase : AllureFileUtils.listFilesByRegex(".*-testcase\\.json", new File(reportDir, "data"))) {
                allureTestCases.add(mapper.readValue(testCase, AllureTestCase.class));
            }
        }
        return allureTestCases;
    }

    public List<TestSuiteResult> getTestSuiteResults() {
        if (testSuiteResults == null) {
            testSuiteResults = new ArrayList<>();
            for (File file : listTestSuiteFiles(resultsDir)) {
                testSuiteResults.add(JAXB.unmarshal(file, TestSuiteResult.class));
            }
        }
        return testSuiteResults;
    }

    private static File createResultsDirectoryFromResources(String... resources) {
        try {
            File resultsDirectory = Files.createTempDirectory("results").toFile();
            for (String resource : resources) {
                IOUtils.copy(
                        AllureReportGenerationRule.class.getClassLoader().getResourceAsStream(resource),
                        new FileOutputStream(new File(resultsDirectory, UUID.randomUUID().toString() + "-testsuite.xml"))
                );
            }
            return resultsDirectory;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
