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
    
    private boolean validateXML = true;

    public AllureReportGenerator(final File... inputDirectories) {
        this.inputDirectories = inputDirectories;
    }

    public void generate(File reportDirectory) {
        File reportDataDirectory = new File(reportDirectory, DATA_SUFFIX);

        if (!(reportDataDirectory.exists() || reportDataDirectory.mkdirs())) {
            throw new RuntimeException(
                    String.format("Can't create data directory <%s>", reportDataDirectory.getAbsolutePath())
            );
        }
        copyAttachments(inputDirectories, reportDataDirectory);

        TestSuiteFiles testSuiteFiles = new TestSuiteFiles(validateXML, inputDirectories);
        String testRun = testSuiteFiles.generateTestRun();
        
        for (final DataProvider provider : dataProviders) {
            provider.provide(testRun, reportDataDirectory);
        }

    }

    public void setValidateXML(boolean validateXML) {
        this.validateXML = validateXML;
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

    public static void copyAttachments(File[] dirs, File outputDirectory) {
        for (File attach : listAttachmentFiles(dirs)) {
            try {
                copyAttachment(attach, new File(outputDirectory, attach.getName()));
            } catch (IOException e) {
                throw new ReportGenerationException(e);
            }
        }
    }

    public static void copyAttachment(File srcFile, File destFile) throws IOException {
        if (!srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())) {
            FileUtils.copyFile(srcFile, destFile);
        }
    }
}
