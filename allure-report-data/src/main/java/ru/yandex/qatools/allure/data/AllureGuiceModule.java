package ru.yandex.qatools.allure.data;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import ru.yandex.qatools.allure.data.converters.DefaultTestCaseConverter;
import ru.yandex.qatools.allure.data.converters.TestCaseConverter;
import ru.yandex.qatools.allure.data.index.DefaultAttachmentIndex;
import ru.yandex.qatools.allure.data.io.AttachmentReader;
import ru.yandex.qatools.allure.data.io.EnvironmentReader;
import ru.yandex.qatools.allure.data.io.Reader;
import ru.yandex.qatools.allure.data.io.ResultDirectories;
import ru.yandex.qatools.allure.data.io.TestCaseReader;
import ru.yandex.qatools.allure.data.io.TestSuiteReader;
import ru.yandex.qatools.allure.data.plugins.AttachmentIndex;
import ru.yandex.qatools.allure.data.plugins.PluginClassLoader;
import ru.yandex.qatools.allure.data.plugins.PluginLoader;
import ru.yandex.qatools.allure.data.plugins.PluginLoaderSpi;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;
import ru.yandex.qatools.commons.model.Environment;

import java.io.File;

/**
 * eroshenkoam
 * 03/02/15
 */
public class AllureGuiceModule extends AbstractModule {

    private File[] inputDirectories;

    private ClassLoader classLoader;

    private AttachmentIndex attachmentIndex;

    public AllureGuiceModule(ClassLoader classLoader, File... inputDirectories) {
        this.classLoader = classLoader;
        this.inputDirectories = inputDirectories;
        this.attachmentIndex = new DefaultAttachmentIndex(inputDirectories);
    }

    @Override
    protected void configure() {
        bind(File[].class).annotatedWith(ResultDirectories.class).toInstance(inputDirectories);
        bind(ClassLoader.class).annotatedWith(PluginClassLoader.class).toInstance(classLoader);
        bind(AttachmentIndex.class).toInstance(attachmentIndex);

        bind(new TypeLiteral<Reader<TestSuiteResult>>() {
        }).to(TestSuiteReader.class);
        bind(new TypeLiteral<Reader<TestCaseResult>>() {
        }).to(TestCaseReader.class);
        bind(new TypeLiteral<Reader<Environment>>() {
        }).to(EnvironmentReader.class);
        bind(new TypeLiteral<Reader<AttachmentInfo>>() {
        }).to(AttachmentReader.class);

        bind(PluginLoader.class).to(PluginLoaderSpi.class);

        bind(TestCaseConverter.class).to(DefaultTestCaseConverter.class);
    }
}
