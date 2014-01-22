package ru.yandex.qatools.allure.report;

import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugins.annotations.Execute;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.reporting.MavenReportException;

import java.util.Locale;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 1/20/14
 */
@Mojo(name = "report")
public class AllureReport extends AbstractAllureReport {

    /**
     * {@inheritDoc}
     */
    public String getOutputName() {
        return plugin.getArtifactId();
    }

    @Override
    protected void executeReport(Locale locale) throws MavenReportException {
        Sink sink = getSink();

        sink.head();
        sink.title();
        sink.text(getName(locale));
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
}
