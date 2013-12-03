package ru.yandex.qatools.allure.data;

import org.apache.commons.io.FileUtils;
import ru.yandex.qatools.allure.data.generators.TestRun;
import ru.yandex.qatools.allure.data.generators.TestSuiteFiles;
import ru.yandex.qatools.allure.data.utils.AllureReportUtils;

import java.io.*;


/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 31.10.13
 */
public class AllureReportGenerator extends ReportGenerator {

    private static final String ATTACHMENTS_MASK = ".+-attachment\\.\\w+";

    public AllureReportGenerator(File... inputDirectories) {
        super(inputDirectories);
    }

    @Override
    public void generate(File outputDirectory) {
        copyAttachments(inputDirectories, outputDirectory);

        TestRun testRun = new TestSuiteFiles(inputDirectories).generateTestRun();

        testRun.generateXUnitData().serialize(outputDirectory);
        testRun.generateTestCasesData().serialize(outputDirectory);
        testRun.generateGraphData().serialize(outputDirectory);
    }

    public static File[] getAttachmentsFiles(File... dirs) {
        return AllureReportUtils.listFiles(dirs, ATTACHMENTS_MASK);
    }

    public static void copyAttachments(File[] dirs, File outputDirectory) {
        for (File attach : getAttachmentsFiles(dirs)) {
            try {
                FileUtils.copyFile(attach, new File(outputDirectory, attach.getName()));
            } catch (IOException ignored) {
            }
        }
    }
}
