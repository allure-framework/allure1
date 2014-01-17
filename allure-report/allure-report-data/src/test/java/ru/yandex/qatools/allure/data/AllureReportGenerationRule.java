package ru.yandex.qatools.allure.data;

import ru.yandex.qatools.allure.model.TestSuiteResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.rules.ExternalResource;
import org.apache.commons.io.FileUtils;

import java.util.ArrayList;
import javax.xml.bind.JAXB;
import java.util.List;
import java.io.File;

import static ru.yandex.qatools.allure.config.AllureNamingUtils.listTestSuiteFiles;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/10/13
 */
public class AllureReportGenerationRule extends ExternalResource {

    private final File reportDir;

    private final File resultsDir;

    private AllureXUnit allureXUnit;

    private List<TestSuiteResult> testSuiteResults;

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
    }

    public AllureXUnit getXUnitData() throws Exception {
        if (allureXUnit == null) {
            ObjectMapper mapper = new ObjectMapper();
            allureXUnit = mapper.readValue(new File(reportDir, "data/xunit.json"), AllureXUnit.class);
        }
        return allureXUnit;
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
