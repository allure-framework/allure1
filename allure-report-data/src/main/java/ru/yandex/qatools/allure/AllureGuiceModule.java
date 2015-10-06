package ru.yandex.qatools.allure;

import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.TypeLiteral;
import ru.qatools.properties.PropertyLoader;
import ru.yandex.qatools.allure.converters.TestCaseConverter;
import ru.yandex.qatools.allure.converters.DefaultTestCaseConverter;
import ru.yandex.qatools.allure.io.Reader;
import ru.yandex.qatools.allure.io.ResultDirectories;
import ru.yandex.qatools.allure.io.TestCaseReader;
import ru.yandex.qatools.allure.io.TestSuiteReader;
import ru.yandex.qatools.allure.plugins.AttachmentsIndex;
import ru.yandex.qatools.allure.plugins.DefaultAttachmentsIndex;
import ru.yandex.qatools.allure.plugins.DefaultEnvironment;
import ru.yandex.qatools.allure.plugins.DefaultPluginLoader;
import ru.yandex.qatools.allure.plugins.DefaultPluginsIndex;
import ru.yandex.qatools.allure.plugins.Environment;
import ru.yandex.qatools.allure.plugins.Plugin;
import ru.yandex.qatools.allure.plugins.PluginClassLoader;
import ru.yandex.qatools.allure.plugins.PluginLoader;
import ru.yandex.qatools.allure.plugins.PluginsIndex;
import ru.yandex.qatools.allure.plugins.ReportConfig;
import ru.yandex.qatools.allure.utils.AllurePropertyProvider;
import ru.yandex.qatools.allure.utils.AllureReportUtils;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static ru.yandex.qatools.allure.AllureConstants.ENVIRONMENT_FILE_NAME;
import static ru.yandex.qatools.allure.AllureConstants.PROPERTY_TEST_RUN_ID;
import static ru.yandex.qatools.allure.AllureConstants.PROPERTY_TEST_RUN_NAME;
import static ru.yandex.qatools.allure.AllureConstants.PROPERTY_TEST_RUN_URL;
import static ru.yandex.qatools.allure.AllureConstants.REPORT_CONFIG_FILE_NAME;
import static ru.yandex.qatools.allure.AllureUtils.listAttachmentFilesSafe;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 03.02.15
 */
public class AllureGuiceModule extends AbstractModule {

    private final Path[] inputDirectories;

    private final ClassLoader classLoader;

    public AllureGuiceModule(ClassLoader classLoader, Path... inputDirectories) {
        this.classLoader = classLoader;
        this.inputDirectories = inputDirectories;
    }

    @Override
    protected void configure() {
        bind(Path[].class).annotatedWith(ResultDirectories.class).toInstance(inputDirectories);
        bind(ClassLoader.class).annotatedWith(PluginClassLoader.class).toInstance(classLoader);

        bind(new TypeLiteral<Reader<TestSuiteResult>>() {
        }).to(TestSuiteReader.class);
        bind(new TypeLiteral<Reader<TestCaseResult>>() {
        }).to(TestCaseReader.class);

        bind(PluginLoader.class).to(DefaultPluginLoader.class);
        bind(TestCaseConverter.class).to(DefaultTestCaseConverter.class);
    }

    @Provides
    @Singleton
    protected Environment provideEnvironment() {
        Properties properties = AllureReportUtils.loadProperties(ENVIRONMENT_FILE_NAME, inputDirectories);
        Map<String, String> map = new HashMap<>(Maps.fromProperties(properties));
        String id = map.remove(PROPERTY_TEST_RUN_ID);
        String name = map.remove(PROPERTY_TEST_RUN_NAME);
        String url = map.remove(PROPERTY_TEST_RUN_URL);

        Map<String, String> parameters = Collections.unmodifiableMap(map);
        return new DefaultEnvironment(id, name, url, parameters);
    }

    @Provides
    @Singleton
    protected ReportConfig provideConfig() {
        return PropertyLoader.newInstance().withPropertyProvider(
                new AllurePropertyProvider(REPORT_CONFIG_FILE_NAME, inputDirectories)
        ).populate(ReportConfig.class);
    }

    @Provides
    @Singleton
    protected AttachmentsIndex provideAttachmentsIndex() {
        List<AttachmentInfo> infoList = new ArrayList<>();
        for (Path file : listAttachmentFilesSafe(inputDirectories)) {
            infoList.add(AllureReportUtils.createAttachmentInfo(file));
        }
        return new DefaultAttachmentsIndex(infoList);
    }

    @Provides
    @Singleton
    protected PluginsIndex providePluginsIndex(Injector injector) {
        PluginLoader loader = injector.getInstance(PluginLoader.class);
        List<Plugin> plugins = loader.loadPlugins();
        return new DefaultPluginsIndex(plugins);
    }
}
