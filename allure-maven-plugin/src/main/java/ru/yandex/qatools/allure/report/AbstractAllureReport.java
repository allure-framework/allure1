package ru.yandex.qatools.allure.report;

import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import ru.yandex.qatools.allure.config.AllureConfig;

import java.io.File;
import java.util.Locale;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 1/20/14
 */
public abstract class AbstractAllureReport extends AbstractMavenReport {

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject project;

    @SuppressWarnings("unused")
    @Parameter(property = "allure.report.directory", defaultValue = "${project.reporting.outputDirectory}")
    private String outputDirectory;

    @Parameter(alias = "allure.results.directory", required = false)
    private File allureResultsDirectory = AllureConfig.newInstance().getResultsDirectory();

    @Parameter
    private Dependency reportFace = new Dependency(
            "ru.yandex.qatools.allure",
            "allure-report-face",
            "${allure.version}",
            "war"
    );

    @Parameter(defaultValue = "${session}", required = true, readonly = true)
    protected MavenSession mavenSession;

    @Component
    protected BuildPluginManager pluginManager;

    @Component
    protected PluginDescriptor plugin;

    @Component
    protected Renderer siteRenderer;


    @Override
    protected final MavenProject getProject() {
        return project;
    }

    @Override
    protected final Renderer getSiteRenderer() {
        return siteRenderer;
    }

    @Override
    public String getName(Locale locale) {
        return "Allure";
    }

    @Override
    public String getOutputName() {
        return plugin.getArtifactId();
    }

    @Override
    public final String getOutputDirectory() {
        return outputDirectory;
    }

    protected final Dependency getReportFace() {
        return reportFace;
    }

    protected final MavenSession getMavenSession() {
        return mavenSession;
    }

    protected final BuildPluginManager getPluginManager() {
        return pluginManager;
    }

    public final File getAllureReportDirectory() {
        return new File(getReportOutputDirectory(), plugin.getArtifactId());
    }

    public File getAllureResultsDirectory() {
        return allureResultsDirectory;
    }

    @Override
    public String getDescription(Locale locale) {
        return "Extended report on the test results of the project.";
    }

}
