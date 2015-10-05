package ru.yandex.qatools.allure.data;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import ru.qatools.properties.PropertyLoader;
import ru.yandex.qatools.allure.data.converters.DefaultTestCaseConverter;
import ru.yandex.qatools.allure.data.converters.TestCaseConverter;
import ru.yandex.qatools.allure.data.io.Reader;
import ru.yandex.qatools.allure.data.io.ResultDirectories;
import ru.yandex.qatools.allure.data.io.TestCaseReader;
import ru.yandex.qatools.allure.data.io.TestSuiteReader;
import ru.yandex.qatools.allure.data.plugins.DefaultEnvironment;
import ru.yandex.qatools.allure.data.plugins.Environment;
import ru.yandex.qatools.allure.data.plugins.ReportConfig;
import ru.yandex.qatools.allure.data.plugins.AttachmentsIndex;
import ru.yandex.qatools.allure.data.plugins.DefaultAttachmentsIndex;
import ru.yandex.qatools.allure.data.plugins.DefaultPluginsIndex;
import ru.yandex.qatools.allure.data.plugins.PluginClassLoader;
import ru.yandex.qatools.allure.data.plugins.PluginLoader;
import ru.yandex.qatools.allure.data.plugins.PluginLoaderSpi;
import ru.yandex.qatools.allure.data.plugins.PluginsIndex;
import ru.yandex.qatools.allure.data.utils.AllurePropertyProvider;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.nio.file.Path;
import java.util.Properties;

import static ru.yandex.qatools.allure.AllureConstants.ENVIRONMENT_FILE_NAME;
import static ru.yandex.qatools.allure.AllureConstants.REPORT_CONFIG_FILE_NAME;
import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.loadProperties;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 03.02.15
 */
public class AllureGuiceModule extends AbstractModule {

    private final Path[] inputDirectories;

    private final ClassLoader classLoader;

    private final ReportConfig config;

    private final Environment environment;

    public AllureGuiceModule(ClassLoader classLoader, Path... inputDirectories) {
        this.classLoader = classLoader;
        this.inputDirectories = inputDirectories;
        this.config = loadConfig(REPORT_CONFIG_FILE_NAME, ReportConfig.class);

        Properties properties = loadProperties(ENVIRONMENT_FILE_NAME, inputDirectories);
        this.environment = new DefaultEnvironment(properties);
    }

    @Override
    protected void configure() {
        bind(Path[].class).annotatedWith(ResultDirectories.class).toInstance(inputDirectories);
        bind(ClassLoader.class).annotatedWith(PluginClassLoader.class).toInstance(classLoader);
        bind(ReportConfig.class).toInstance(config);
        bind(Environment.class).toInstance(environment);

        bind(new TypeLiteral<Reader<TestSuiteResult>>() {
        }).to(TestSuiteReader.class);
        bind(new TypeLiteral<Reader<TestCaseResult>>() {
        }).to(TestCaseReader.class);

        bind(PluginLoader.class).to(PluginLoaderSpi.class);
        bind(Key.get(AttachmentsIndex.class)).to(DefaultAttachmentsIndex.class);
        bind(PluginsIndex.class).to(DefaultPluginsIndex.class);

        bind(TestCaseConverter.class).to(DefaultTestCaseConverter.class);
    }

    /**
     * Load configuration class from specified files in input directories.
     */
    private <T> T loadConfig(String fileName, Class<T> configClass) {
        return PropertyLoader.newInstance().withPropertyProvider(
                new AllurePropertyProvider(fileName, inputDirectories)
        ).populate(configClass);
    }
}
