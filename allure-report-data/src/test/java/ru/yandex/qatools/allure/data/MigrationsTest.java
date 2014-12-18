package ru.yandex.qatools.allure.data;

import org.junit.ClassRule;
import org.junit.Test;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.util.List;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 17.12.14
 */
public class MigrationsTest {

    @ClassRule
    public static AllureReportGenerationRule allureRule =
            new AllureReportGenerationRule("1.3-severity.testsuite.xml");

    @Test
    public void severityTest() throws Exception {
        List<AllureTestCase> testCases = allureRule.getTestCasesData();
        List<AllureTestCase> withCriticalSeverityName = findByName(testCases, "criticalSeverity");
        assertThat("should find only one testcase with name 'criticalSeverity'",
                withCriticalSeverityName, hasSize(1));
        assertThat("severity migration failed in case empty labels element exists",
                withCriticalSeverityName.get(0).getSeverity(), equalTo(SeverityLevel.CRITICAL));
        assertThat("migration shouldn't lose other test data",
                withCriticalSeverityName.get(0).getSteps(), hasSize(2));

        List<AllureTestCase> withBlockerSeverityName = findByName(testCases,  "blockerSeverity");
        assertThat("should find only one testcase with name 'blockerSeverity'",
                withBlockerSeverityName, hasSize(1));
        assertThat("severity migration failed in case test already contains some labels",
                withBlockerSeverityName.get(0).getSeverity(), equalTo(SeverityLevel.BLOCKER));

        List<AllureTestCase> withTrivialSeverityName = findByName(testCases,  "trivialSeverity");
        assertThat("should find only one testcase with name 'trivialSeverity'",
                withTrivialSeverityName, hasSize(1));
        assertThat("severity migration failed in case labels element doesn't exist",
                withTrivialSeverityName.get(0).getSeverity(), equalTo(SeverityLevel.TRIVIAL));
    }

    public List<AllureTestCase> findByName(List<AllureTestCase> testCases, String criticalSeverity) {
        return select(
                testCases,
                having(on(AllureTestCase.class).getName(), equalTo(criticalSeverity))
        );
    }
}
