package ru.yandex.qatools.allure.data;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import ru.qatools.properties.PropertyLoader;
import ru.yandex.qatools.allure.data.converters.DefaultTestCaseConverter;
import ru.yandex.qatools.allure.data.converters.TestCaseConverter;
import ru.yandex.qatools.allure.data.io.EnvironmentReader;
import ru.yandex.qatools.allure.data.io.Reader;
import ru.yandex.qatools.allure.data.io.ResultDirectories;
import ru.yandex.qatools.allure.data.io.TestCaseReader;
import ru.yandex.qatools.allure.data.io.TestSuiteReader;
import ru.yandex.qatools.allure.data.plugins.AttachmentsIndex;
import ru.yandex.qatools.allure.data.plugins.DefaultAttachmentsIndex;
import ru.yandex.qatools.allure.data.plugins.DefaultPluginsIndex;
import ru.yandex.qatools.allure.data.plugins.PluginClassLoader;
import ru.yandex.qatools.allure.data.plugins.PluginLoader;
import ru.yandex.qatools.allure.data.plugins.PluginLoaderSpi;
import ru.yandex.qatools.allure.data.plugins.PluginsIndex;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.commons.model.Environment;

import java.nio.file.Path;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 03.02.15
 */
public class AllureGuiceModule extends AbstractModule {

    private final Path[] inputDirectories;

    private final ClassLoader classLoader;

    private final AllureReportConfig config;

    public AllureGuiceModule(ClassLoader classLoader, Path... inputDirectories) {
        this.classLoader = classLoader;
        this.inputDirectories = inputDirectories;
        this.config = PropertyLoader.newInstance().populate(AllureReportConfig.class);
    }

    @Override
    protected void configure() {
        bind(Path[].class).annotatedWith(ResultDirectories.class).toInstance(inputDirectories);
        bind(ClassLoader.class).annotatedWith(PluginClassLoader.class).toInstance(classLoader);
        bind(AllureReportConfig.class).toInstance(config);

        bind(new TypeLiteral<Reader<TestSuiteResult>>() {
        }).to(TestSuiteReader.class);
        bind(new TypeLiteral<Reader<TestCaseResult>>() {
        }).to(TestCaseReader.class);
        bind(new TypeLiteral<Reader<Environment>>() {
        }).to(EnvironmentReader.class);

        bind(PluginLoader.class).to(PluginLoaderSpi.class);
        bind(Key.get(AttachmentsIndex.class)).to(DefaultAttachmentsIndex.class);
        bind(PluginsIndex.class).to(DefaultPluginsIndex.class);

        bind(TestCaseConverter.class).to(DefaultTestCaseConverter.class);
    }
}
