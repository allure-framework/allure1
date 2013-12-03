package ru.yandex.qatools.allure.data;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.listFiles;

/**
* @author Dmitry Baev charlie@yandex-team.ru
*         Date: 31.10.13
*/
public class ReportDataGenerateTest {

    private static final String GRAPH_FILE_NAME = "graph.json";

    private static final String XUNIT_FILE_NAME = "xunit.json";

    private static final String ATTACHMENT_FILE_NAME = "example-uid-attachment.xml";

    private File outputDirectory;

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void generate() throws Exception {
        outputDirectory = folder.newFolder();
        AllureReportGenerator reportGenerator = new AllureReportGenerator(
                new File(ClassLoader.getSystemResource("testdata3").getFile())
        );

        reportGenerator.generate(outputDirectory);
    }

    @Test
    public void testCasesPackCreatedTest() throws Exception {
        File[] files = listFiles(outputDirectory, GRAPH_FILE_NAME);
        assertEquals(
                String.format("There is no \"%s\" file.", GRAPH_FILE_NAME),
                1,
                files.length
        );

        AllureGraph allureGraph = new ObjectMapper().readValue(files[0], AllureGraph.class);
        assertEquals("There is no test cases in pack.", 2, allureGraph.getTestCases().size());
    }

    @Test
    public void allureXUnitCreatedTest() throws Exception {
        File[] files = listFiles(outputDirectory, XUNIT_FILE_NAME);
        assertEquals(
                String.format("There is no \"%s\" file.", XUNIT_FILE_NAME),
                1,
                files.length
        );

        AllureXUnit allureXUnit = new ObjectMapper().readValue(files[0], AllureXUnit.class);
        assertEquals("There is no test suites in pack", 2, allureXUnit.getTestSuites().size());
    }

    @Test
    public void attachmentsCopiedTest() throws Exception {
        assertEquals(
                "Attachment file doesn't copied.",
                1,
                AllureReportGenerator.getAttachmentsFiles(outputDirectory).length
        );

        assertEquals(
                "Attachment copied with wrong name.",
                ATTACHMENT_FILE_NAME,
                AllureReportGenerator.getAttachmentsFiles(outputDirectory)[0].getName()
        );
    }

    @Test
    public void allureXUnitTimeTest() throws Exception {
        File[] files = listFiles(outputDirectory, XUNIT_FILE_NAME);
        AllureXUnit allureXUnit = new ObjectMapper().readValue(files[0], AllureXUnit.class);
        assertThat(
                "Wrong test suites pack start time.",
                allureXUnit.getTime().getStart(),
                is(Long.valueOf("1384780017376"))
        );

        assertThat(
                "Wrong test suites pack stop time.",
                allureXUnit.getTime().getStop(),
                is(Long.valueOf("1384780017556"))
        );

        assertThat(
                "Wrong test suites pack duration.",
                allureXUnit.getTime().getDuration(),
                is((long) 180)
        );
    }
}
