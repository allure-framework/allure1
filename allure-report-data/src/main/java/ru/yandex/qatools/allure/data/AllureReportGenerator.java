package ru.yandex.qatools.allure.data;

import org.apache.commons.io.FileUtils;
import ru.yandex.qatools.allure.data.providers.*;

import java.io.*;
import java.util.Arrays;
import java.util.List;

import static ru.yandex.qatools.allure.config.AllureNamingUtils.listAttachmentFiles;


/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 31.10.13
 */
public class AllureReportGenerator {

    private static final String DATA_SUFFIX = "data";

    private List<DataProvider> dataProviders = Arrays.asList(defaultProviders());

    protected File[] inputDirectories;

    public AllureReportGenerator(final File... inputDirectories) {
        this.inputDirectories = inputDirectories;
    }

    public void generate(final File reportDirectory) {
        final File reportDataDirectory = new File(reportDirectory, DATA_SUFFIX);

        if (!(reportDataDirectory.exists() || reportDataDirectory.mkdirs())) {
            throw new RuntimeException(
                    String.format("Can't create data directory <%s>", reportDataDirectory.getAbsolutePath())
            );
        }
        copyAttachments(inputDirectories, reportDataDirectory);

        final TestSuiteFiles testSuiteFiles = new TestSuiteFiles(inputDirectories);
        final String testRun = testSuiteFiles.generateTestRun();
        
        for (final File skippedSuiteFile: testSuiteFiles.getSkippedSuiteFiles()){
            //TODO: here we can log invalid suite files (https://github.com/allure-framework/allure-core/issues/183)
        }

        for (final DataProvider provider : dataProviders) {
            provider.provide(testRun, reportDataDirectory);
        }

    }

    public static DataProvider[] defaultProviders() {
        return new DataProvider[]{
                new XUnitDataProvider(),
                new GraphDataProvider(),
                new TestCasesDataProvider(),
                new BehaviorDataProvider(),
                new DefectsDataProvider()
        };
    }

    public static void copyAttachments(final File[] dirs, final File outputDirectory) {
        for (final File attach : listAttachmentFiles(dirs)) {
            try {
                copyAttachment(attach, new File(outputDirectory, attach.getName()));
            } catch (IOException e) {
                throw new ReportGenerationException(e);
            }
        }
    }

    public static void copyAttachment(final File srcFile, final File destFile) throws IOException {
        if (!srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            FileUtils.copyFile(srcFile, destFile);
        }
    }
}
