package ru.yandex.qatools.allure.data;

import ru.yandex.qatools.allure.data.providers.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

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
        final File reportDataDirectory = createDirectory(reportDirectory, DATA_SUFFIX);

        long attachmentsSize = copyAttachments(inputDirectories, reportDataDirectory);
        final AtomicLong reportSize = new AtomicLong(attachmentsSize);
        final String testRun = testRunGenerator.generate();

        ExecutorService service = Executors.newFixedThreadPool(dataProviders.size());
        for (final DataProvider provider : dataProviders) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    reportSize.addAndGet(provider.provide(testRun, reportDataDirectory));
                }
            });
        }
        try {
            service.shutdown();
            service.awaitTermination(1, TimeUnit.HOURS);
            writeReportSize(reportSize.get(), reportDataDirectory);
        } catch (InterruptedException e) {
            throw new ReportGenerationException(e);
        }
    }

    private static File createDirectory(File parent, String name) {
        File created = new File(parent, name);
        checkDirectory(created);
        return created;
    }

    private static void checkDirectory(File directory) {
        if (!(directory.exists() || directory.mkdirs())) {
            throw new RuntimeException(
                    String.format("Can't create data directory <%s>", directory.getAbsolutePath())
            );
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

    @SuppressWarnings("unused")
    public void setValidateXML(boolean validateXML) {
        this.validateXML = validateXML;
    }

    public TestRunGenerator getTestRunGenerator() {
        return testRunGenerator;
    }

    @SuppressWarnings("unused")
    public File[] getInputDirectories() {
        return inputDirectories;
    }
}
