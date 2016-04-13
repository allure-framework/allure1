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
    private Reader<AttachmentInfo> attachmentReader;

    @Inject
    private TestCaseConverter converter;

    @Inject
    private PluginManager pluginManager;

    public AllureReportGenerator(File... inputDirectories) {
        this(AllureReportGenerator.class.getClassLoader(), inputDirectories);
    }

    public AllureReportGenerator(ClassLoader pluginClassLoader, File... inputDirectories) {
        this(new AllureGuiceModule(pluginClassLoader, inputDirectories));
    }

    /**
     * For testing only
     */
    AllureReportGenerator(AbstractModule module) {
        Guice.createInjector(module).injectMembers(this);
    }

    public void generate(File outputDirectory) {
        ReportWriter writer = new ReportWriter(outputDirectory);
        generate(writer);
    }

    public void generate(ReportWriter writer) {
        if (!testCaseReader.iterator().hasNext()) {
            throw new ReportGenerationException("Could not find any allure results");
        }

        writer.writeIndexHtml(pluginManager.getPluginsNames());

        for (TestCaseResult result : testCaseReader) {
            pluginManager.prepare(result);

            AllureTestCase testCase = converter.convert(result);
            pluginManager.prepare(testCase);
            pluginManager.process(testCase);
            writer.write(testCase);
        }

        for (Environment environment : environmentReader) {
            pluginManager.prepare(environment);
            pluginManager.process(environment);
        }

        for (AttachmentInfo attachment : attachmentReader) {
            pluginManager.prepare(attachment);
            writer.write(attachment);
        }

        pluginManager.writePluginData(writer);
        pluginManager.writePluginResources(writer);
        pluginManager.writePluginList(writer);
        pluginManager.writePluginWidgets(writer);

        writer.writeReportInfo();
    }
}
