package ru.yandex.qatools.allure.report;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 1/20/14
 */
public abstract class AbstractAllureReport extends AbstractMojo {

    @SuppressWarnings("unused")
    @Parameter(property = "allure.report.directory", defaultValue = "${project.build.directory}/site/")
    private String outputDirectory;

}
