package ru.yandex.qatools.allure.data.io;

import ru.yandex.qatools.allure.data.AllureTestCase;
import ru.yandex.qatools.allure.data.plugins.PluginData;
import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.commons.model.Environment;

import java.io.File;
import java.util.List;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.serialize;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.02.15
 */
public class ReportWriter {

    private File outputDirectory;

    private long start;

    private long stop;

    public ReportWriter(File outputDirectory) {
        this.outputDirectory = outputDirectory;
        this.start = System.currentTimeMillis();
    }

    public void write(AllureTestCase testCase) {
        serialize(outputDirectory, testCase.getUid() + "-testcase.json", testCase);
    }

    public void write(List<PluginData> pluginData) {
        if (pluginData == null) {
            return;
        }
        for (PluginData data : pluginData) {
            write(data);
        }
    }

    public void write(PluginData pluginData) {
        if (pluginData == null) {
            return;
        }
        serialize(outputDirectory, pluginData.getName(), pluginData.getData());
    }

    public void write(Object object) {
//        do nothing
    }

    public void close() {
        stop = System.currentTimeMillis();
    }
}
