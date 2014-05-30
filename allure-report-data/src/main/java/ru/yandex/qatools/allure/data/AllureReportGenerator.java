package ru.yandex.qatools.allure.data;

import ru.yandex.qatools.allure.data.providers.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.copyAttachments;
import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.writeReportSize;


/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 31.10.13
 */
public class AllureReportGenerator {

    private static final String DATA_SUFFIX = "data";

    private List<DataProvider> dataProviders = Arrays.asList(defaultProviders());

    protected File[] inputDirectories;

    protected TestRunGenerator testRunGenerator;

    private boolean validateXML = true;

    public AllureReportGenerator(final File... inputDirectories) {
        this.inputDirectories = inputDirectories;
        this.testRunGenerator = new TestRunGenerator(validateXML, inputDirectories);
    }

    public void generate(File reportDirectory) {
        File reportDataDirectory = new File(reportDirectory, DATA_SUFFIX);

        if (!(reportDataDirectory.exists() || reportDataDirectory.mkdirs())) {
            throw new RuntimeException(
                    String.format("Can't create data directory <%s>", reportDataDirectory.getAbsolutePath())
            );
        }
        long reportSize = copyAttachments(inputDirectories, reportDataDirectory);

        String testRun = testRunGenerator.generate();

        for (final DataProvider provider : dataProviders) {
            reportSize += provider.provide(testRun, reportDataDirectory);
        }

        writeReportSize(reportSize, reportDataDirectory);
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


    public void setValidateXML(boolean validateXML) {
        this.validateXML = validateXML;
    }

    public TestRunGenerator getTestRunGenerator() {
        return testRunGenerator;
    }

    public File[] getInputDirectories() {
        return inputDirectories;
    }
}
