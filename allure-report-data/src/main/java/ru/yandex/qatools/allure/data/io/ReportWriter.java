package ru.yandex.qatools.allure.data.io;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.data.AllureReportInfo;
import ru.yandex.qatools.allure.data.AllureTestCase;
import ru.yandex.qatools.allure.data.AttachmentInfo;
import ru.yandex.qatools.allure.data.plugins.PluginData;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.serialize;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.02.15
 */
public class ReportWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportWriter.class);

    public static final String REPORT_JSON = "report.json";

    private File outputDirectory;

    private long start;

    private long size = 0;

    public ReportWriter(File outputDirectory) {
        this.outputDirectory = outputDirectory;
        this.start = System.currentTimeMillis();
    }

    public void write(AllureTestCase testCase) {
        size += serialize(outputDirectory, testCase.getUid() + "-testcase.json", testCase);
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
        size += serialize(outputDirectory, pluginData.getName(), pluginData.getData());
    }

    public void write(AttachmentInfo attachmentInfo) {
        if (attachmentInfo == null) {
            return;
        }
        File from = new File(attachmentInfo.getPath());
        File to = new File(outputDirectory, attachmentInfo.getName());
        try {
            if (!from.getCanonicalPath().equals(to.getCanonicalPath())) {
                FileUtils.copyFile(from, to);
            }
        } catch (IOException e) {
            LOGGER.error(String.format("Can't copy attachment %s from %s",
                    attachmentInfo.getName(), attachmentInfo.getPath()));
            LOGGER.trace("Can't copy attachment", e);
        }
    }

    public void close() {
        long stop = System.currentTimeMillis();
        AllureReportInfo info = new AllureReportInfo();
        info.setTime(stop - start);
        info.setSize(size);
        serialize(outputDirectory, REPORT_JSON, info);
    }
}
