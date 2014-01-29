package ru.yandex.qatools.allure.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.junit.rules.ExternalResource;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXB;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.qatools.allure.config.AllureNamingUtils.listTestSuiteFiles;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/10/13
 */
public class AllureReportGenerationRule extends ExternalResource {

    private final File reportDataDir;

    private final File resultsDir;

    private AllureXUnit allureXUnit;

    private AllureDefects allureDefects;

    private List<TestSuiteResult> testSuiteResults;

    public AllureReportGenerationRule(String resultsPath) {
        this(FileUtils.getTempDirectory(), resultsPath);
    }

    public AllureReportGenerationRule(File reportDataDir, String resultsPath) {
        this(reportDataDir, ClassLoader.getSystemResource(resultsPath));
    }

    public AllureReportGenerationRule(File reportDataDir, URL resultsURI) {
        this(reportDataDir, new File(resultsURI.getFile()));
    }

    public AllureReportGenerationRule(File reportDataDir, File resultsDir) {
        this.reportDataDir = reportDataDir;
        this.resultsDir = resultsDir;
    }


    protected void before() throws Throwable {
        AllureReportGenerator reportGenerator = new AllureReportGenerator(resultsDir);
        reportGenerator.generate(reportDataDir);
    }

    protected void after() {
        FileUtils.deleteQuietly(this.reportDataDir);
    }

    public AllureXUnit getXUnitData() throws Exception {
        if (allureXUnit == null) {
            ObjectMapper mapper = new ObjectMapper();
            allureXUnit = mapper.readValue(new File(reportDataDir, "xunit.json"), AllureXUnit.class);
        }
        return allureXUnit;
    }

    public AllureDefects getDefectsData() throws Exception {
        if (allureDefects == null) {
            ObjectMapper mapper = new ObjectMapper();
            allureDefects = mapper.readValue(new File(reportDataDir, "defects.json"), AllureDefects.class);
        }
        return allureDefects;
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

}
