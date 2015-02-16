package ru.yandex.qatools.allure.data;

import ru.yandex.qatools.allure.data.converters.DefaultTestCaseConverter;
import ru.yandex.qatools.allure.data.converters.TestCaseConverter;
import ru.yandex.qatools.allure.data.io.Reader;
import ru.yandex.qatools.allure.data.io.ReportWriter;
import ru.yandex.qatools.allure.data.io.TestCaseReader;
import ru.yandex.qatools.allure.data.io.TestSuiteReader;
import ru.yandex.qatools.allure.data.plugins.PluginLoader;
import ru.yandex.qatools.allure.data.plugins.PluginLoaderImpl;
import ru.yandex.qatools.allure.data.plugins.PluginManager;
import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.commons.model.Environment;

import java.io.File;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.02.15
 */
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class AllureReportGenerator {

    private final File[] inputDirectories;

    private Reader<TestCaseResult> testCaseReader;

    private Reader<Attachment> attachmentReader;

    private Reader<Environment> environmentReader;

    private TestCaseConverter converter;

    private PluginManager pluginManager;

    public AllureReportGenerator(final File... inputDirectories) {
        this.inputDirectories = inputDirectories;
        this.testCaseReader = new TestCaseReader(new TestSuiteReader(inputDirectories));
        this.converter = new DefaultTestCaseConverter();

        PluginLoader loader = new PluginLoaderImpl(getClass().getClassLoader());
        this.pluginManager = new PluginManager(loader);
    }


    public void generate(File outputDirectory) {
        ReportWriter writer = new ReportWriter(outputDirectory);

        process(attachmentReader, writer);
        process(environmentReader, writer);
        process(testCaseReader, writer);

        writer.write(pluginManager.getData(AllureTestCase.class));
        writer.close();
    }

    private <T> void process(Reader<T> reader, ReportWriter writer) {
        for (T t : reader) {
            pluginManager.prepare(t);
            pluginManager.process(t);

            writer.write(t);
        }
    }
}
