package ru.yandex.qatools.allure.data;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import ru.yandex.qatools.allure.data.converters.TestCaseConverter;
import ru.yandex.qatools.allure.data.io.Reader;
import ru.yandex.qatools.allure.data.io.Writer;
import ru.yandex.qatools.allure.data.plugins.AttachmentsProvider;
import ru.yandex.qatools.allure.data.plugins.EnrichPlugin;
import ru.yandex.qatools.allure.data.plugins.EnvironmentProvider;
import ru.yandex.qatools.allure.data.plugins.Plugin;
import ru.yandex.qatools.allure.data.plugins.TabPlugin;
import ru.yandex.qatools.allure.data.plugins.WidgetPlugin;
import ru.yandex.qatools.allure.data.utils.PluginUtils;
import ru.yandex.qatools.allure.model.TestCaseResult;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.createDirectory;
import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.writeAllureReportInfo;


/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 31.10.13
 */
public class AllureReportGenerator {

    private static final String DATA_SUFFIX = "data";

    private ClassLoader classLoader;

    protected final File[] inputDirectories;

    private boolean validateXML = true;

    @Inject
    private Reader<TestCaseResult> resultReader;

    @Inject
    private Writer<AllureTestCase> writer;

    @Inject
    private TestCaseConverter resultConverter;

    @Inject
    private List<Plugin> processPlugins;

    @Inject
    private List<TabPlugin> tabPlugins;

    @Inject
    private List<WidgetPlugin> widgetPlugins;

    @Inject
    private List<EnrichPlugin> enrichPlugins;

    @Inject
    private AttachmentsProvider attachmentsProvider;

    @Inject
    private EnvironmentProvider environmentProvider;

    public AllureReportGenerator(final File... inputDirectories) {
        this.classLoader = getClass().getClassLoader();
        this.inputDirectories = inputDirectories;
    }

    public void generate(File reportDirectory) {
        long start = System.currentTimeMillis();

        final File reportDataDirectory = createDirectory(reportDirectory, DATA_SUFFIX);
        final AtomicLong reportSize = new AtomicLong(0);

        Injector injector = Guice.createInjector(new AppInjector(reportDataDirectory, inputDirectories));
        injector.injectMembers(this);

        for (TestCaseResult result : resultReader) {
            AllureTestCase testCase = resultConverter.convert(result);

            for (Plugin plugin : enrichPlugins) {
                plugin.process(testCase);
            }

            for (Plugin plugin : processPlugins) {
                plugin.process(PluginUtils.clone(testCase));
            }

            reportSize.addAndGet(writer.write(testCase));
        }

        for (TabPlugin plugin : tabPlugins) {
            reportSize.addAndGet(plugin.getTabData().write(reportDataDirectory));
        }

        for (WidgetPlugin plugin : widgetPlugins) {
            reportSize.addAndGet(plugin.getWidgetData().write(reportDataDirectory));
        }

        reportSize.addAndGet(attachmentsProvider.provide());
        reportSize.addAndGet(environmentProvider.provide());

        long stop = System.currentTimeMillis();

        AllureReportInfo reportInfo = new AllureReportInfo();
        reportInfo.setSize(reportSize.get());
        reportInfo.setTime(stop - start);

        writeAllureReportInfo(reportInfo, reportDataDirectory);
    }

    @SuppressWarnings("unused")
    public ClassLoader getClassLoader() {
        return classLoader;
    }

    @SuppressWarnings("unused")
    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @SuppressWarnings("unused")
    public File[] getInputDirectories() {
        return inputDirectories;
    }

    @SuppressWarnings("unused")
    public void setValidateXML(boolean validateXML) {
        this.validateXML = validateXML;
    }
}
