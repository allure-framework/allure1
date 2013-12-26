package ru.yandex.qatools.allure.report;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import ru.yandex.qatools.allure.data.AllureReportGenerator;

import java.util.*;
import java.io.*;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;

/**
 * @author Artem Eroshenko eroshenkoam
 *         6/7/13, 6:06 PM
 */
@SuppressWarnings("unused")
@Mojo(name = "allure-maven-plugin", defaultPhase = LifecyclePhase.SITE, requiresDependencyResolution = ResolutionScope.COMPILE_PLUS_RUNTIME)
public class AllureMavenPlugin extends AbstractAllureReportPlugin {

    protected static final String DEFAULT_ALLURE_REPORT_PATH = "site/allure-maven-plugin";

    protected static final String DEFAULT_ALLURE_REPORT_DATA_PATH = "/data";

    private static final String TEST_SUITE_FILES_REGEXP = ".*-testsuite\\.xml";

    private static final String TEST_CASE_FILES_REGEXP = ".*-testcase\\.xml";

    private static final String FAQ = "https://github.com/allure-framework/allure-core/blob/master/docs/FAQ.md";

    private static final String ALLURE_TEAM = "mailto:allure@yandex-team.ru";

    @Parameter(alias = "allure.report.path", defaultValue = DEFAULT_ALLURE_REPORT_PATH, required = false)
    protected String allureReportPath;

    @Parameter(alias = "allure.results.path", defaultValue = DEFAULT_ALLURE_REPORT_PATH, required = false)
    protected String allureResultsPath;


    @Parameter(alias = "allure.face.unpack", defaultValue = "true", required = false)
    protected boolean allureFaceUnpack;

    @Parameter
    private Dependency reportFace = new Dependency(
            "ru.yandex.qatools.allure",
            "allure-report-face",
            "${allure.version}",
            "war"
    );

    @Parameter(defaultValue = "${project.build.directory}")
    private String outputDirectory;

    @Parameter(defaultValue = "Allure Report")
    private String reportName;

    @Override
    protected final String getOutputDirectory() {
        return outputDirectory;
    }

    @Override
    public final File getReportOutputDirectory() {
        return getAllureReportDirectory();
    }

    @Override
    public String getName(Locale locale) {
        return reportName;
    }

    public String getAllureResultsPath() {
        return allureResultsPath;
    }

    public String getAllureReportPath() {
        return allureReportPath;
    }

    public String getAllureReportDataPath() {
        return getAllureReportPath() + DEFAULT_ALLURE_REPORT_DATA_PATH;
    }

    public File getAllureResultsDirectory() {
        return new File(getOutputDirectory(), getAllureResultsPath());
    }

    public File getAllureReportDirectory() {
        return new File(getOutputDirectory(), getAllureReportPath());
    }

    public File getAllureReportDataDirectory() {
        return new File(getOutputDirectory(), getAllureReportDataPath());
    }

    public boolean isAllureFaceUnpack() {
        return allureFaceUnpack;
    }

    @Override
    protected final void executeReport(Locale locale) throws MavenReportException {
        File resultsDirectory = getAllureResultsDirectory();
        File reportDirectory = getAllureReportDirectory();
        File reportDataDirectory = getAllureReportDataDirectory();

        Sink sink = getSink();

        sink.head();
        sink.title();
        sink.text(getName(locale));
        sink.title_();
        sink.head_();

        sink.body();

        sink.lineBreak();

        String directoryIOErrorMessageTemplate = "Problem communicate with directory <%s>. [exists:%s,write:%s]";

        getLog().info(String.format("Analyse allure report directory <%s>", reportDirectory));

        if (!((reportDirectory.exists() || reportDirectory.mkdirs()) && reportDirectory.canWrite())) {
            String error = String.format(directoryIOErrorMessageTemplate,
                    reportDirectory.getAbsolutePath(),
                    reportDirectory.exists(),
                    reportDirectory.canWrite());
            getLog().error(error + FAQ);
            sink.text(error);
            sink.link(FAQ);
            sink.text(FAQ);
            sink.link_();

            return;
        }

        getLog().info(String.format("Analyse allure report data directory <%s>", reportDataDirectory));
        if (!((reportDataDirectory.exists() || reportDataDirectory.mkdirs()) && reportDataDirectory.canWrite())) {
            String error = String.format(directoryIOErrorMessageTemplate,
                    reportDataDirectory.getAbsolutePath(),
                    reportDataDirectory.exists(),
                    reportDataDirectory.canWrite());
            getLog().error(error + FAQ);
            sink.text(error);
            sink.link(FAQ);
            sink.text(FAQ);
            sink.link_();

            return;
        }

        generateData(resultsDirectory, reportDataDirectory);

        //TODO: split this plugin on two with goals: generate-data, copy-face, default (include both)
        if (!allureFaceUnpack) {
            return;
        }

        if (unpackReport()) {
            sink.rawText("<meta http-equiv=\"refresh\" content=\"0;url=allure-maven-plugin/index.html\" />");
            sink.link("allure-maven-plugin/index.html");
            sink.figure();
            sink.figureGraphics("allure-maven-plugin/img/allure-dashboard.png");
            sink.figure_();
            sink.link_();

        } else {
            sink.figure();
            sink.figureGraphics("allure-maven-plugin/img/report-failed.png");
            sink.figure_();
            sink.lineBreak();
        }

        sink.body_();

        sink.flush();
        sink.close();
    }

    private void generateData(File resultsDirectory, File reportDataDirectory) {
        getLog().info(String.format("Generate report data from results directory <%s> to report data directory <%s>",
                resultsDirectory,
                reportDataDirectory));
        AllureReportGenerator reportGenerator = new AllureReportGenerator(resultsDirectory);
        reportGenerator.generate(reportDataDirectory);
        getLog().info(String.format("Generate report data complete to directory <%s>",
                reportDataDirectory));
    }

    private boolean unpackReport() {
        try {
            executeMojo(
                    getDependencyPlugin(),
                    goal("unpack"),
                    getDependencyPluginConfiguration(),
                    executionEnvironment(getProject(), mavenSession, pluginManager)
            );
        } catch (MojoExecutionException e) {
            getLog().error(getErrorMessage(), e);
            return false;
        }
        return true;
    }

    private String getErrorMessage() {
        return String.format(
                "Please, see FAQ %s or contact with allure team %s.",
                FAQ,
                ALLURE_TEAM
        );
    }

    private Plugin getDependencyPlugin() {
        return plugin("org.apache.maven.plugins", "maven-dependency-plugin", "2.8");
    }

    private Xpp3Dom getDependencyPluginConfiguration() {
        return configuration(
                element(
                        "artifactItems",
                        element("artifactItem",
                                element("groupId", reportFace.getGroupId()),
                                element("artifactId", reportFace.getArtifactId()),
                                element("version", reportFace.getVersion()),
                                element("overWrite", "true"),
                                element("outputDirectory", getOutputDirectory() + "/" + getAllureReportPath()),
                                element("type", reportFace.getPackaging())
                        )
                )
        );
    }
}
