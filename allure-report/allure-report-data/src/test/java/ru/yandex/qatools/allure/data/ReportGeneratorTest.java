package ru.yandex.qatools.allure.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 21.10.13
 */
public class ReportGeneratorTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void generateWithEmptyInputTest() throws Exception {
        File output = folder.newFolder();
        AllureReportGenerator reportGenerator = new AllureReportGenerator();
        reportGenerator.generate(output);
    }

    @Test
    public void attachmentsCountTest() throws Exception {
        assertEquals(
                "Not all attachment files are found.",
                6,
                AllureReportGenerator.getAttachmentsFiles(getTestDataDirs()).length
        );
    }

    @Test
    public void attachmentsCopiedTest() throws Exception {
        File outputDirectory = folder.newFolder();
        AllureReportGenerator.copyAttachments(getTestDataDirs(), outputDirectory);
        assertEquals(
                "Not all attachment files are copied.",
                6,
                AllureReportGenerator.getAttachmentsFiles(outputDirectory).length
        );
    }

    public static File[] getTestDataDirs() {
        return new File[]{
                new File(ClassLoader.getSystemResource("testdata1").getFile()),
                new File(ClassLoader.getSystemResource("testdata2").getFile()),
                new File(ClassLoader.getSystemResource("testdata3").getFile())
        };
    }

}

