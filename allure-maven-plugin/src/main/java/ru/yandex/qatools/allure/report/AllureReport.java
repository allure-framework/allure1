package ru.yandex.qatools.allure.report;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import ru.yandex.qatools.allure.data.AllureReportGenerator;

import java.io.File;
import java.util.Locale;

import static org.twdata.maven.mojoexecutor.MojoExecutor.*;
import static org.twdata.maven.mojoexecutor.MojoExecutor.configuration;
import static org.twdata.maven.mojoexecutor.MojoExecutor.element;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 1/20/14
 */
@SuppressWarnings("unused")
@Mojo(name = "report", defaultPhase = LifecyclePhase.SITE)
public class AllureReport extends AbstractAllureReport {


    @Override
    protected void executeReport(Locale locale) throws MavenReportException {
        File allureReportDirectory = getAllureReportDirectory();
        File allureResultsDirectory = getAllureResultsDirectory();

        AllureReportGenerator generator = new AllureReportGenerator(allureResultsDirectory);
        generator.generate(allureReportDirectory);

        unpackAllureReportFace();

        render(getSink(), getName(locale));
    }

    private void render(Sink sink, String title) {
        sink.head();
        sink.title();
        sink.text(title);
        sink.title_();
        sink.head_();
        sink.body();

        sink.lineBreak();

        sink.rawText("<meta http-equiv=\"refresh\" content=\"0;url=allure-maven-plugin/index.html\" />");
        sink.link("allure-maven-plugin/index.html");

        sink.body_();
        sink.flush();
        sink.close();
    }

    private void unpackAllureReportFace() throws MavenReportException {
        try {
            executeMojo(
                    plugin("org.apache.maven.plugins", "maven-dependency-plugin", "2.8"),
                    goal("unpack"),
                    getDependencyPluginConfiguration(),

                    executionEnvironment(getProject(), getMavenSession(), getPluginManager())
            );

        } catch (MojoExecutionException e) {
            throw new MavenReportException(e.getMessage());
        }
    }

    private Xpp3Dom getDependencyPluginConfiguration() {
        return configuration(
                element(
                        "artifactItems",
                        element("artifactItem",
                                element("groupId", getReportFace().getGroupId()),
                                element("artifactId", getReportFace().getArtifactId()),
                                element("version", getReportFace().getVersion()),
                                element("overWrite", "true"),
                                element("outputDirectory", getAllureReportDirectory().getAbsolutePath()),
                                element("type", getReportFace().getPackaging())
                        )
                )
        );
    }

}
