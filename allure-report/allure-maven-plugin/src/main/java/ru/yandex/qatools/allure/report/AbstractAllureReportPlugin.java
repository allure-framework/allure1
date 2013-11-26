package ru.yandex.qatools.allure.report;

import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.BuildPluginManager;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;

import java.util.Locale;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.11.13
 */
public abstract class AbstractAllureReportPlugin extends AbstractMavenReport {

    protected static final String ALLURE_REPORT_DIR = "site/allure-maven-plugin";

    protected static final String ALLURE_REPORT_DATA_DIR = ALLURE_REPORT_DIR + "/data";

    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    protected MavenProject project;

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
    public final String getOutputName() {
        return plugin.getArtifactId();
    }

    @Override
    public String getDescription(Locale locale) {
        return "Extended report on the test results of the project.";
    }
}
