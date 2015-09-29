package ru.yandex.qatools.allure.data;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.yandex.qatools.allure.data.io.ReportWriter;

import java.io.File;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.02.15
 */
public class AllureReportGenerator {

    private final Injector injector;

    public AllureReportGenerator(File... inputDirectories) {
        this(AllureReportGenerator.class.getClassLoader(), inputDirectories);
    }

    public AllureReportGenerator(ClassLoader pluginClassLoader, File... inputDirectories) {
        this(new AllureGuiceModule(pluginClassLoader, inputDirectories));
    }

    /**
     * For testing only.
     */
    AllureReportGenerator(AbstractModule module) {
        injector = Guice.createInjector(module);
    }

    public void generate(File outputDirectory) {
        ReportWriter writer = new ReportWriter(outputDirectory);
        generate(writer);
    }

    public void generate(ReportWriter writer) {
        AllureReportLifecycle lifecycle = injector.getInstance(AllureReportLifecycle.class);
        lifecycle.generate(writer);
    }
}
