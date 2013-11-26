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

    private static final String TEST_CASES_PACK_FILE_NAME = "testcases-pack.json";

    private static final String TEST_SUITES_PACK_FILE_NAME = "testsuites-pack.json";

    private static final String LIST_FILES_FILE_NAME = "list-files.json";

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
        File[] files = listFiles(outputDirectory, TEST_CASES_PACK_FILE_NAME);
        assertEquals(
                String.format("There is no \"%s\" file.", TEST_CASES_PACK_FILE_NAME),
                1,
                files.length
        );

        TestCasesPack testCasesPack = new ObjectMapper().readValue(files[0], TestCasesPack.class);
        assertEquals("There is no test cases in pack.", 2, testCasesPack.getTestCases().size());
    }

    @Test
    public void testSuitesPackCreatedTest() throws Exception {
        File[] files = listFiles(outputDirectory, TEST_SUITES_PACK_FILE_NAME);
        assertEquals(
                String.format("There is no \"%s\" file.", TEST_SUITES_PACK_FILE_NAME),
                1,
                files.length
        );

        TestSuitesPack testSuitesPack = new ObjectMapper().readValue(files[0], TestSuitesPack.class);
        assertEquals("There is no test suites in pack", 2, testSuitesPack.getTestSuites().size());
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
    public void listFilesCreatedTest() throws Exception {
        File[] files = listFiles(outputDirectory, LIST_FILES_FILE_NAME);
        assertEquals(
                String.format("There is no \"%s\" file.", LIST_FILES_FILE_NAME),
                1,
                files.length
        );

        ListFiles listFiles = new ObjectMapper().readValue(files[0], ListFiles.class);
        assertEquals(
                String.format("Wrong list files created."),
                2,
                listFiles.getFiles().size()
        );
    }

    @Test
    public void testSuiteTimeTest() throws Exception {
        File[] files = listFiles(outputDirectory, TEST_SUITES_PACK_FILE_NAME);
        TestSuitesPack testSuitesPack = new ObjectMapper().readValue(files[0], TestSuitesPack.class);
        assertThat(
                "Wrong test suites pack start time.",
                testSuitesPack.getTime().getStart(),
                is(Long.valueOf("1384780017376"))
        );

        assertThat(
                "Wrong test suites pack stop time.",
                testSuitesPack.getTime().getStop(),
                is(Long.valueOf("1384780017556"))
        );

        assertThat(
                "Wrong test suites pack duration.",
                testSuitesPack.getTime().getDuration(),
                is((long) 180)
        );
    }
}
