package ru.yandex.qatools.allure.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.model.ModelProperties;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.xml.bind.JAXB;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import static ch.lambdaj.Lambda.*;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * Created by eroshenkoam on 12/10/13.
 */
public class XUnitDataTest {

    @ClassRule
    public static TemporaryFolder tmpFolder = new TemporaryFolder();

    private static AllureXUnit allureXUnitData;

    private static List<TestSuiteResult> testSuiteResults;

    @BeforeClass
    public static void generateReport() throws Exception {
        File allureDataDir = tmpFolder.newFolder();
        ModelProperties modelProperties = new ModelProperties();
        File allureResultsDir = new File(ClassLoader.getSystemResource(modelProperties.getResultsPath()).getFile());

        AllureReportGenerator allureReportGenerator = new AllureReportGenerator(allureResultsDir);
        allureReportGenerator.generate(allureDataDir);

        ObjectMapper mapper = new ObjectMapper();
        allureXUnitData = mapper.readValue(new File(allureDataDir, "xunit.json"), AllureXUnit.class);

        testSuiteResults = new ArrayList<>();
        for (String path : allureResultsDir.list(new RegexFileFilter(TestSuiteFiles.TEST_SUITES_MASK))) {
            testSuiteResults.add(JAXB.unmarshal(new File(allureResultsDir, path), TestSuiteResult.class));
        }
    }

    @Test
    public void xUnitStartTimeShouldBeMinimumStartTimeOfAllTestSuites() {
        assertThat(allureXUnitData.getTime().getStart(), equalTo(minFrom(testSuiteResults).getStart()));
    }

    @Test
    public void xUnitStopTimeShouldBeMaximumStopTimeOfAllTestSuites() {
        assertThat(allureXUnitData.getTime().getStop(), equalTo(maxFrom(testSuiteResults).getStop()));
    }

    @Test
    public void xUnitDurationTimeShouldDifferenceBetweenStopTimeAndStartTime() {
        assertThat(allureXUnitData.getTime().getDuration(),
                equalTo(allureXUnitData.getTime().getStop() - allureXUnitData.getTime().getStart()));
    }

    @Test
    public void xUnitTestSuitesCountMustBeEqualsResultsTestSuitesCount() {
        assertThat(allureXUnitData.getTestSuites().size(), equalTo(testSuiteResults.size()));
    }

    @Test
    public void xUnitTestCasesCountMustBeEqualsResultsTestCasesCount() {
        int xUnitTestCasesCount = sum(allureXUnitData.getTestSuites(), on(AllureTestSuite.class).getTestCases().size());
        int resultsTestCasesCount = sum(testSuiteResults, on(TestSuiteResult.class).getTestCases().size());
        assertThat(xUnitTestCasesCount, equalTo(resultsTestCasesCount));
    }
}
