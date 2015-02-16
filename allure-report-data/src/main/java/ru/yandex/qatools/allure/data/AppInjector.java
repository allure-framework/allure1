//package ru.yandex.qatools.allure.data;
//
//import com.google.inject.AbstractModule;
//import com.google.inject.TypeLiteral;
//import ru.yandex.qatools.allure.data.converters.DefaultTestCaseConverter;
//import ru.yandex.qatools.allure.data.converters.TestCaseConverter;
//import ru.yandex.qatools.allure.data.io.Reader;
//import ru.yandex.qatools.allure.data.io.ReportDirectory;
//import ru.yandex.qatools.allure.data.io.ResultDirectory;
//import ru.yandex.qatools.allure.data.io.TestCaseReader;
//import ru.yandex.qatools.allure.data.io.TestCaseWriter;
//import ru.yandex.qatools.allure.data.io.TestSuiteReader;
//import ru.yandex.qatools.allure.data.io.Writer;
//import ru.yandex.qatools.allure.data.plugins.AttachmentsProvider;
//import ru.yandex.qatools.allure.data.plugins.EnvironmentProvider;
//import ru.yandex.qatools.allure.data.plugins.Plugin;
//import ru.yandex.qatools.allure.data.plugins.PluginManagerJava;
//import ru.yandex.qatools.allure.data.plugins.PreparePlugin;
//import ru.yandex.qatools.allure.data.plugins.TabPlugin;
//import ru.yandex.qatools.allure.data.utils.ServiceLoaderUtils;
//import ru.yandex.qatools.allure.model.TestCaseResult;
//import ru.yandex.qatools.allure.model.TestSuiteResult;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import static java.util.Collections.unmodifiableList;
//
///**
// * eroshenkoam
// * 03/02/15
// */
//public class AppInjector extends AbstractModule {
//
//    private File[] resultDirectories;
//
//    private File reportDirectory;
//
//    private List<Plugin> processPlugins = new ArrayList<>();
//
//    private List<TabPlugin> tabPlugins = new ArrayList<>();
//
//    private List<PreparePlugin> enrichPlugins = new ArrayList<>();
//
//    public AppInjector(File reportDirectory, File... resultDirectories) {
//        this.reportDirectory = reportDirectory;
//        this.resultDirectories = resultDirectories;
//
//        //TODO: it's just an example, we should think about plugins a bit more
//        for (Plugin plugin : ServiceLoaderUtils.load(getClass().getClassLoader(), Plugin.class)) {
//            //            pre-process test cases
//            if (plugin instanceof PreparePlugin) {
//                enrichPlugins.add((PreparePlugin) plugin);
//                continue;
//            }
//
//            //            process test cases
//            if (plugin instanceof TabPlugin) {
//                tabPlugins.add((TabPlugin) plugin);
//            }
//
//            processPlugins.add(plugin);
//        }
//    }
//
//    @Override
//    protected void configure() {
//        if (reportDirectory.mkdirs()) {
//            throw new ReportGenerationException("Can't create report directory");
//        }
//
//        bind(File[].class).annotatedWith(ResultDirectory.class).toInstance(resultDirectories);
//        bind(File.class).annotatedWith(ReportDirectory.class).toInstance(reportDirectory);
//
//        bind(new TypeLiteral<Reader<TestSuiteResult>>() {}).to(new TypeLiteral<TestSuiteReader>() {});
//        bind(new TypeLiteral<Reader<TestCaseResult>>() {}).to(new TypeLiteral<TestCaseReader>() {});
//
//        bind(new TypeLiteral<Writer<AllureTestCase>>() {}).to(new TypeLiteral<TestCaseWriter>() {});
//
//        bind(new TypeLiteral<TestCaseConverter>() {}).to(new TypeLiteral<DefaultTestCaseConverter>() {});
//
//        bind(EnvironmentProvider.class);
//        bind(AttachmentsProvider.class);
//        bind(PluginManagerJava.class);
//
//        bind(new TypeLiteral<List<TabPlugin>>() {}).toInstance(unmodifiableList(tabPlugins));
//        bind(new TypeLiteral<List<Plugin>>() {}).toInstance(unmodifiableList(processPlugins));
//        bind(new TypeLiteral<List<PreparePlugin>>() {}).toInstance(unmodifiableList(enrichPlugins));
//    }
//}
