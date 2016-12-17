package ru.yandex.qatools.allure.testng;

import static java.util.Collections.*;
import static javax.xml.bind.JAXB.unmarshal;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static ru.yandex.qatools.allure.commons.AllureFileUtils.*;

import java.io.*;
import java.util.*;
import org.junit.*;
import org.junit.rules.TemporaryFolder;
import org.testng.TestNG;
import ru.yandex.qatools.allure.model.*;
import ru.yandex.qatools.allure.utils.AllureResultsUtils;

public class AllureTestListenerGroupsTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private File resultsDir;

    @Before
    public void setUp() throws IOException {
        resultsDir = folder.newFolder();
        AllureResultsUtils.setResultsDirectory(resultsDir);
    }

    @Test // see https://github.com/allure-framework/allure-core/issues/880
    public void reportContainsTestForGroups() {
        // GIVEN: an TestNG suite with groups 
        TestNG testNG = new TestNG(false);
        testNG.setTestSuites(singletonList(getClass().getClassLoader().getResource("suite-groups.xml").getFile()));

        // WHEN: executing
        testNG.run();

        // THEN: report only contains results for included groups
        List<File> files = listTestSuiteFiles(resultsDir);
        assertThat(files, hasSize(1));
        File file = files.get(0);
        TestSuiteResult result = unmarshal(file, TestSuiteResult.class);
        assertThat(result.getTestCases(), hasSize(2));
        List<String> status = new ArrayList<>();
        for (TestCaseResult test : result.getTestCases()) {
            status.add(test.getName() + ":" + test.getStatus());
        }
        assertThat(status, containsInAnyOrder("inactiveIncludedTest:PENDING", "activeIncludedTest:PASSED"));
    }
}
