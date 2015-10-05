package ru.yandex.qatools.allure.data;

import com.google.inject.Inject;
import ru.yandex.qatools.allure.data.converters.TestCaseConverter;
import ru.yandex.qatools.allure.data.io.Reader;
import ru.yandex.qatools.allure.data.io.ReportWriter;
import ru.yandex.qatools.allure.data.plugins.AttachmentsIndex;
import ru.yandex.qatools.allure.data.plugins.PluginManager;
import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 29.09.15
 */
public class AllureReportLifecycle {

    @Inject
    private Reader<TestCaseResult> testCaseReader;

    @Inject
    private TestCaseConverter converter;

    @Inject
    private PluginManager pluginManager;

    @Inject
    private AttachmentsIndex attachmentsIndex;

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

        for (AttachmentInfo info : attachmentsIndex.findAll()) {
            writer.write(info);
        }

        pluginManager.writePluginData(writer);
        pluginManager.writePluginResources(writer);
        pluginManager.writePluginList(writer);
        pluginManager.writePluginWidgets(writer);

        writer.writeReportInfo();
    }
}
