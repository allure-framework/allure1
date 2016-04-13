package ru.yandex.qatools.allure.data;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import ru.yandex.qatools.allure.data.converters.DefaultTestCaseConverter;
import ru.yandex.qatools.allure.data.converters.TestCaseConverter;
import ru.yandex.qatools.allure.data.index.DefaultAttachmentsIndex;
import ru.yandex.qatools.allure.data.index.DefaultPluginsIndex;
import ru.yandex.qatools.allure.data.io.AttachmentReader;
import ru.yandex.qatools.allure.data.io.EnvironmentReader;
import ru.yandex.qatools.allure.data.io.Reader;
import ru.yandex.qatools.allure.data.io.ResultDirectories;
import ru.yandex.qatools.allure.data.io.TestCaseReader;
import ru.yandex.qatools.allure.data.io.TestSuiteReader;
import ru.yandex.qatools.allure.data.plugins.AttachmentsIndex;
import ru.yandex.qatools.allure.data.plugins.DefaultPluginLoader;
import ru.yandex.qatools.allure.data.plugins.Plugin;
import ru.yandex.qatools.allure.data.plugins.PluginClassLoader;
import ru.yandex.qatools.allure.data.plugins.PluginLoader;
import ru.yandex.qatools.allure.data.plugins.PluginsIndex;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.commons.model.Environment;

import java.io.File;
import java.util.List;

/**
 * eroshenkoam
 * 03/02/15
 */
public class AllureGuiceModule extends AbstractModule {

    private File[] inputDirectories;

    private ClassLoader classLoader;

    public AllureGuiceModule(ClassLoader classLoader, File... inputDirectories) {
        this.classLoader = classLoader;
        this.inputDirectories = inputDirectories;
    }

    @Override
    protected void configure() {
        bind(File[].class).annotatedWith(ResultDirectories.class).toInstance(inputDirectories);
        bind(ClassLoader.class).annotatedWith(PluginClassLoader.class).toInstance(classLoader);

        bind(new TypeLiteral<Reader<TestSuiteResult>>() {
        }).to(TestSuiteReader.class);
        bind(new TypeLiteral<Reader<TestCaseResult>>() {
        }).to(TestCaseReader.class);
        bind(new TypeLiteral<Reader<Environment>>() {
        }).to(EnvironmentReader.class);
        bind(new TypeLiteral<Reader<AttachmentInfo>>() {
        }).to(AttachmentReader.class);

        bind(PluginLoader.class).to(DefaultPluginLoader.class);
        bind(AttachmentsIndex.class).to(DefaultAttachmentsIndex.class);

        bind(TestCaseConverter.class).to(DefaultTestCaseConverter.class);
    }

    @Provides
    @Singleton
    protected PluginsIndex providePluginsIndex(Injector injector) {
        PluginLoader loader = injector.getInstance(PluginLoader.class);
        List<Plugin> plugins = loader.loadPlugins();
        return new DefaultPluginsIndex(plugins);
    }
}
