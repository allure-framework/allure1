package ru.yandex.qatools.allure.data;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import ru.yandex.qatools.allure.data.converters.TestCaseConverter;
import ru.yandex.qatools.allure.data.io.Reader;
import ru.yandex.qatools.allure.data.io.ReportWriter;
import ru.yandex.qatools.allure.data.plugins.PluginManager;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.commons.model.Environment;

import java.io.File;
import java.lang.Thread;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.02.15
 */
public class AllureReportGenerator {

    @Inject
    private Reader<TestCaseResult> testCaseReader;

    @Inject
    private Reader<Environment> environmentReader;

    @Inject
    private TestCaseConverter converter;

    @Inject
    private PluginManager pluginManager;

    public AllureReportGenerator(File... inputDirectories) {
        this(Thread.currentThread().getContextClassLoader(), inputDirectories);
    }

    public AllureReportGenerator(ClassLoader pluginClassLoader, File... inputDirectories) {
        this(new AppInjector(pluginClassLoader, inputDirectories));
    }

    /**
     * For testing only
     */
    AllureReportGenerator(AbstractModule injector) {
        Guice.createInjector(injector).injectMembers(this);
    }

    public void generate(File outputDirectory) {
        ReportWriter writer = new ReportWriter(outputDirectory);
        generate(writer);
    }

    public void generate(ReportWriter writer) {
        for (TestCaseResult result : testCaseReader) {
            AllureTestCase testCase = converter.convert(result);
            pluginManager.prepare(testCase);
            pluginManager.process(testCase);
            writer.write(testCase);
        }

        for (Environment environment : environmentReader) {
            pluginManager.prepare(environment);
            pluginManager.process(environment);
        }

        writer.close();
    }
}
