package ru.yandex.qatools.allure.io

import freemarker.template.Configuration
import freemarker.template.Template
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.apache.commons.io.FilenameUtils
import ru.yandex.qatools.allure.AllureReportInfo
import ru.yandex.qatools.allure.AllureTestCase
import ru.yandex.qatools.allure.AttachmentInfo
import ru.yandex.qatools.allure.ReportGenerationException
import ru.yandex.qatools.allure.plugins.Environment
import ru.yandex.qatools.allure.plugins.PluginData

import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import static java.nio.file.Files.newBufferedWriter
import static ru.yandex.qatools.allure.utils.AllureReportUtils.createDirectory
import static ru.yandex.qatools.allure.utils.AllureReportUtils.serialize

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 21.04.15
 */
@Slf4j
@CompileStatic
class ReportWriter {

    public static final String DATA_DIRECTORY_NAME = "data"

    public static final String PLUGINS_DIRECTORY_NAME = "plugins"

    public static final String REPORT_JSON = "report.json"

    public static final String TESTCASE_SUFFIX = "-testcase.json"

    private final Path outputDataDirectory

    private final Path outputPluginsDirectory

    private final Path indexHtml

    private final Long start

    private final Environment environment

    private Long size = 0

    ReportWriter(Path outputDirectory, Environment environment) {
        this.outputDataDirectory = createDirectory(outputDirectory, DATA_DIRECTORY_NAME)
        this.outputPluginsDirectory = createDirectory(outputDirectory, PLUGINS_DIRECTORY_NAME)
        this.indexHtml = outputDirectory.resolve("index.html")
        this.start = System.currentTimeMillis()
        this.environment = environment
    }

    void write(AllureTestCase testCase) {
        Objects.requireNonNull(testCase)
        serializeToData(testCase.uid + TESTCASE_SUFFIX, testCase);
    }

    void write(PluginData data) {
        Objects.requireNonNull(data)
        serializeToData(data.name, data.data);
    }

    void write(AttachmentInfo attachmentInfo) {
        Objects.requireNonNull(attachmentInfo)
        try {
            Path from = Paths.get(attachmentInfo.path)
            getStreamToDataDirectory(attachmentInfo.source).withCloseable { output ->
                Files.copy(from, output)
            }
        } catch (IOException e) {
            log.error("Can't copy attachment $attachmentInfo.source from $attachmentInfo.path", e)
        }
    }

    void write(String pluginName, URL resource) {
        copyResource(pluginName, resource)
    }

    void writeIndexHtml(List<String> plugins) {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_23)
        cfg.setClassLoaderForTemplateLoading(getClass().classLoader, "")
        try {
            Template template = cfg.getTemplate("index.html.ftl");

            newBufferedWriter(indexHtml, StandardCharsets.UTF_8).withCloseable {
                template.process(["plugins": plugins, "version": "unknown"], it)
            }
        } catch (IOException e) {
            throw new ReportGenerationException(e)
        }
    }

    /**
     * Write allure report info {@link ru.yandex.qatools.allure.AllureReportInfo} to
     * data directory.
     */
    void writeReportInfo() {
        def stop = System.currentTimeMillis()
        def info = new AllureReportInfo(
                id: environment.id,
                name: environment.name,
                url: environment.url,
                time: stop - start,
                size: size
        )
        serialize(getStreamToDataDirectory(REPORT_JSON), info)
    }

    /**
     * Copy given URL resource to writer to plugins/${name} folder
     */
    private void copyResource(String pluginName, URL resource) {
        def resourceName = FilenameUtils.getName(resource.toString())

        getStreamToPluginDirectory(pluginName, resourceName).withCloseable { output ->
            resource.openStream().withCloseable { input ->
                output << input
            }
        }
    }

    /**
     * Serialize given object to file with given name to data directory.
     * @see #size
     * @see ru.yandex.qatools.allure.utils.AllureReportUtils#serialize(java.io.File, java.lang.String, java.lang.Object)
     */
    private void serializeToData(String fileName, Object object) {
        size += serialize(getStreamToDataDirectory(fileName), object)
    }

    /**
     * Get stream to file with given name in data directory.
     * @see #getStream(java.nio.file.Path, java.lang.String)
     */
    private OutputStream getStreamToDataDirectory(String fileName) {
        getStream(outputDataDirectory, fileName)
    }

    /**
     * Get stream to file with given name in plugin directory with specified name.
     * @see #getStream(java.nio.file.Path, java.lang.String)
     */
    private OutputStream getStreamToPluginDirectory(String pluginName, String fileName) {
        def pluginDir = createDirectory(outputPluginsDirectory, pluginName)
        getStream(pluginDir, fileName)
    }

    /**
     * Get output stream file with given name in specified directory.
     * @throw ReportGenerationException if can not create a file or stream.
     */
    private static OutputStream getStream(Path directory, String fileName) {
        try {
            Files.newOutputStream(directory.resolve(fileName))
        } catch (Exception e) {
            log.error("Can't create directory $fileName in data folder", e)
            throw new ReportGenerationException(e)
        }
    }
}
