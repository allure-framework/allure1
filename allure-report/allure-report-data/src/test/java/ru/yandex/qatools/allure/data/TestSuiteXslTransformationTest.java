package ru.yandex.qatools.allure.data;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.qatools.allure.data.utils.AllureReportUtils;
import ru.yandex.qatools.allure.model.AttachmentType;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ru.yandex.qatools.allure.model.Status;

import javax.xml.bind.JAXB;
import java.io.InputStream;
import java.io.StringReader;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 17.10.13
 */
public class TestSuiteXslTransformationTest {

    private final static String XML_FILE_NAME = "testdata3/example-uid-testsuite.xml";

    private final static String XSL_FILE_NAME = "xsl/create-test-pack.xsl";

    private AllureTestSuite allureTestSuite;

    @Before
    public void setUp() throws Exception {
        InputStream xsl = getClass().getClassLoader().getResourceAsStream(XSL_FILE_NAME);
        InputStream xml = getClass().getClassLoader().getResourceAsStream(XML_FILE_NAME);
        String result = AllureReportUtils.applyXslTransformation(xsl, xml);
        allureTestSuite = JAXB.unmarshal(
                new StringReader(result),
                AllureTestSuite.class
        );
    }

    @Test
    public void testSuiteNotNullTest() throws Exception {
        assertNotNull(allureTestSuite);
    }

    @Test
    public void testSuiteUidTest() throws Exception {
        assertThat(allureTestSuite.getUid(), is(notNullValue()));
    }

    @Test
    public void testSuiteTitleTest() throws Exception {
        assertThat(allureTestSuite.getTitle(), is("example-title"));
    }

    @Test
    public void testSuiteTestCasesSizeTest() throws Exception {
        assertThat(allureTestSuite.getTestCases().size(), is(1));
    }

    @Test
    public void testSuiteStatisticTotalTest() throws Exception {
        assertThat(allureTestSuite.getStatistic().getTotal(), is((long) 1));
    }

    @Test
    public void testSuiteStatisticBrokenTest() throws Exception {
        assertThat(allureTestSuite.getStatistic().getBroken(), is((long) 0));
    }

    @Test
    public void testSuiteStatisticPassedTest() throws Exception {
        assertThat(allureTestSuite.getStatistic().getPassed(), is((long) 0));
    }

    @Test
    public void testSuiteStatisticFailedTest() throws Exception {
        assertThat(allureTestSuite.getStatistic().getFailed(), is((long) 1));
    }

    @Test
    public void testSuiteStatisticSkippedTest() throws Exception {
        assertThat(allureTestSuite.getStatistic().getSkipped(), is((long) 0));
    }

    @Test
     public void testSuiteTimeStartTest() throws Exception {
        assertThat(allureTestSuite.getTime().getStart(), is(Long.valueOf("1384780017376")));
    }

    @Test
    public void testSuiteTimeStopTest() throws Exception {
        assertThat(allureTestSuite.getTime().getStop(), is(Long.valueOf("1384780017532")));
    }

    @Test
    public void testSuiteTimeDurationTest() throws Exception {
        assertThat(allureTestSuite.getTime().getDuration(), is((long) 156));
    }

    @Test
    public void testSuiteTestCasesStatusTest() throws Exception {
        assertThat(allureTestSuite.getTestCases().get(0).getStatus(), is(Status.FAILED));
    }

    @Test
    public void testSuiteTestCasesSeverityTest() throws Exception {
        assertThat(allureTestSuite.getTestCases().get(0).getSeverity(), is(SeverityLevel.CRITICAL));
    }

    @Test
    public void testSuiteTestCasesAttachmentTest() throws Exception {
        assertThat(allureTestSuite.getTestCases().get(0).getAttachments().size(), is(1));
        assertThat(allureTestSuite.getTestCases().get(0).getAttachments().get(0).getSource(), is("example-attach-source"));
        assertThat(allureTestSuite.getTestCases().get(0).getAttachments().get(0).getTitle(), is("example-attach-title"));
        assertThat(allureTestSuite.getTestCases().get(0).getAttachments().get(0).getType(), is(AttachmentType.JSON));

    }

    @Test
    public void testSuiteTestCasesFailureTest() throws Exception {
        assertThat(allureTestSuite.getTestCases().get(0).getFailure().getMessage(), is("example-message"));
        assertThat(allureTestSuite.getTestCases().get(0).getFailure().getStackTrace(), is("example-stack-trace"));
    }

    @Test
    public void testSuiteTestCasesStepsTest() throws Exception {
        assertThat(allureTestSuite.getTestCases().get(0).getSteps().size(), is(1));
        assertThat(allureTestSuite.getTestCases().get(0).getSteps().get(0).getTitle(), is("example-step-title"));
    }

    @Test
    public void suiteSuiteTest() throws Exception {

        for (AllureTestCase testCase : allureTestSuite.getTestCases()) {
            assertThat(testCase.getSuiteUid(), is(allureTestSuite.getUid()));
        }
    }
}
