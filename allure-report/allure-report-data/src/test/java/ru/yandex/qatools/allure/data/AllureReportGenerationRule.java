package ru.yandex.qatools.allure.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.junit.rules.ExternalResource;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by eroshenkoam on 12/10/13.
 */
public class AllureReportGenerationRule extends ExternalResource {

    private final File reportDataDir;

    private final File resultsDir;

    private AllureXUnit allureXUnit;

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
        try {
            FileUtils.deleteDirectory(this.reportDataDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AllureXUnit getXUnitData() throws Exception {
        if (allureXUnit == null) {
            ObjectMapper mapper = new ObjectMapper();
            allureXUnit = mapper.readValue(new File(reportDataDir, "xunit.json"), AllureXUnit.class);
        }
        return allureXUnit;
    }

    public List<TestSuiteResult> getTestSuiteResults() {
        if (testSuiteResults == null) {
            testSuiteResults = new ArrayList<>();
            for (String path : resultsDir.list(new RegexFileFilter(TestSuiteFiles.TEST_SUITES_MASK))) {
                testSuiteResults.add(JAXB.unmarshal(new File(resultsDir, path), TestSuiteResult.class));
            }
        }
        return testSuiteResults;
    }

}
