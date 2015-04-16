package ru.yandex.qatools.allure.data.io;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.data.AllureReportInfo;
import ru.yandex.qatools.allure.data.AllureTestCase;
import ru.yandex.qatools.allure.data.AttachmentInfo;
import ru.yandex.qatools.allure.data.ReportGenerationException;
import ru.yandex.qatools.allure.data.plugins.PluginData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.createDirectory;
import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.serialize;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.02.15
 */
//FIXME feels like this class so shitty
public class ReportWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReportWriter.class);

    public static final String DATA_DIRECTORY_NAME = "data";

    public static final String PLUGINS_DIRECTORY_NAME = "plugins";

    public static final String REPORT_JSON = "report.json";

    private final File outputDirectory;

    private final File outputDataDirectory;

    private final File outputPluginsDirectory;

    private long start;

    private long size = 0;

    public ReportWriter(File outputDirectory) {
        this.outputDirectory = outputDirectory;
        this.outputDataDirectory = createDirectory(outputDirectory, DATA_DIRECTORY_NAME);
        this.outputPluginsDirectory = createDirectory(outputDirectory, PLUGINS_DIRECTORY_NAME);
        this.start = System.currentTimeMillis();
    }

    public void write(AllureTestCase testCase) {
        size += serialize(outputDataDirectory, testCase.getUid() + "-testcase.json", testCase);
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
        size += serialize(outputDataDirectory, pluginData.getName(), pluginData.getData());
    }

    public void write(AttachmentInfo attachmentInfo) {
        if (attachmentInfo == null) {
            return;
        }
        File from = new File(attachmentInfo.getPath());
        File to = new File(outputDataDirectory, attachmentInfo.getName());
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

    public OutputStream getPluginResourceOutputStream(String pluginName, String resourceName) {
        File pluginDir = createDirectory(outputPluginsDirectory, pluginName);
        try {
            return new FileOutputStream(new File(pluginDir, resourceName));
        } catch (FileNotFoundException e) {
            LOGGER.error("Can't create file for plugin resource <{}:{}>", pluginName, resourceName);
            throw new ReportGenerationException(e);
        }
    }

    public void close() {
        long stop = System.currentTimeMillis();
        AllureReportInfo info = new AllureReportInfo();
        info.setTime(stop - start);
        info.setSize(size);
        serialize(outputDataDirectory, REPORT_JSON, info);
    }
}
