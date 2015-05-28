package ru.yandex.qatools.allure.data.io

import groovy.util.logging.Slf4j
import org.apache.commons.io.FilenameUtils
import ru.yandex.qatools.allure.data.AllureReportInfo
import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.AttachmentInfo
import ru.yandex.qatools.allure.data.ReportGenerationException
import ru.yandex.qatools.allure.data.plugins.PluginData

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.createDirectory
import static ru.yandex.qatools.allure.data.utils.AllureReportUtils.serialize

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 21.04.15
 */
@Slf4j
class ReportWriter {

    public static final String DATA_DIRECTORY_NAME = "data"

    public static final String PLUGINS_DIRECTORY_NAME = "plugins"

    public static final String REPORT_JSON = "report.json"

    public static final String TESTCASE_SUFFIX = "-testcase.json"

    private final File outputDataDirectory

    private final File outputPluginsDirectory

    private Long start

    private Long size = 0;

    ReportWriter(File outputDirectory) {
        this.outputDataDirectory = createDirectory(outputDirectory, DATA_DIRECTORY_NAME)
        this.outputPluginsDirectory = createDirectory(outputDirectory, PLUGINS_DIRECTORY_NAME)
        this.start = System.currentTimeMillis()
    }

    @SuppressWarnings("GrMethodMayBeStatic")
    void write(Object object) {
        if (object) {
            throw new ReportGenerationException("Can't write object ${object.toString()}")
        }
    }

    void write(Collection items) {
        items.each { write(it) }
    }

    void write(AllureTestCase testCase) {
        serializeToData(testCase.uid + TESTCASE_SUFFIX, testCase);
    }

    void write(PluginData data) {
        serializeToData(data?.name, data?.data);
    }

    void write(AttachmentInfo attachmentInfo) {
        try {
            Path from = Paths.get(attachmentInfo?.path)
            Files.copy(from, getStreamToDataDirectory(attachmentInfo?.name))
        } catch (IOException e) {
            log.error("Can't copy attachment $attachmentInfo.name from $attachmentInfo.path", e)
        }
    }

    void write(String pluginName, URL resource) {
        copyResource(pluginName, resource)
    }

    /**
     * Write allure report info {@link AllureReportInfo} to
     * data directory.
     */
    void writeReportInfo() {
        def stop = System.currentTimeMillis();
        def info = new AllureReportInfo(time: stop - start, size: size)
        serialize(getStreamToDataDirectory(REPORT_JSON), info);
    }

    /**
     * Copy given URL resource to writer to plugins/${name} folder
     */
    protected void copyResource(String pluginName, URL resource) {
        def resourceName = FilenameUtils.getName(resource.toString())
        def output = getStreamToPluginDirectory(pluginName, resourceName)
        output << resource.openStream()
        output.close()
    }

    /**
     * Serialize given object to file with given name to data directory.
     * @see #size
     * @see ru.yandex.qatools.allure.data.utils.AllureReportUtils#serialize(java.io.File, java.lang.String, java.lang.Object)
     */
    protected void serializeToData(String fileName, Object object) {
        size += serialize(getStreamToDataDirectory(fileName), object)
    }

    /**
     * Get stream to file with given name in data directory.
     * @see #getStream(java.io.File, java.lang.String)
     */
    protected OutputStream getStreamToDataDirectory(String fileName) {
        getStream(outputDataDirectory, fileName)
    }

    /**
     * Get stream to file with given name in plugin directory with specified name.
     * @see #getStream(java.io.File, java.lang.String)
     */
    protected OutputStream getStreamToPluginDirectory(String pluginName, String fileName) {
        def pluginDir = createDirectory(outputPluginsDirectory, pluginName);
        getStream(pluginDir, fileName)
    }

    /**
     * Get output stream file with given name in specified directory.
     * @throw ReportGenerationException if can not create a file or stream.
     */
    protected static OutputStream getStream(File directory, String fileName) {
        try {
            new FileOutputStream(new File(directory, fileName))
        } catch (Exception e) {
            log.error("Can't create directory $fileName in data folder", e)
            throw new ReportGenerationException(e)
        }
    }
}
