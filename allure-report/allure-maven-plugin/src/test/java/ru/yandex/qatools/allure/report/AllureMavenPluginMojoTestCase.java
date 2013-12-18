package ru.yandex.qatools.allure.report;

import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.12.13
 */
public class AllureMavenPluginMojoTestCase extends AbstractMojoTestCase {

    static {
        //TODO dirty hack??
        System.setProperty("basedir", getReportPluginBasedir().getAbsolutePath());
    }

    public static File getReportPluginBasedir() {
        return new File("allure-report/allure-maven-plugin");
    }

    public void testGenerateReportWithoutData() throws Exception {

        File pluginConfig = getTestFile("src/test/resources/unit/plugin-config.xml");
        System.out.println(pluginConfig.exists());

        AllureMavenPlugin mojo = (AllureMavenPlugin) lookupMojo("allure-maven-plugin", pluginConfig);
        assertNotNull(mojo);

        PluginDescriptor pluginDescriptor = new PluginDescriptor();
        pluginDescriptor.setArtifactId("allure-maven-plugin");
        mojo.plugin = pluginDescriptor;

        mojo.execute();

        File xunit = new File(getReportPluginBasedir(), "target/test/unit/results/data/xunit.json");
        assertTrue(xunit.exists());

        File graph = new File(getReportPluginBasedir(), "target/test/unit/results/data/graph.json");
        assertTrue(graph.exists());

        File behavior = new File(getReportPluginBasedir(), "target/test/unit/results/data/behavior.json");
        assertTrue(behavior.exists());
    }
}
