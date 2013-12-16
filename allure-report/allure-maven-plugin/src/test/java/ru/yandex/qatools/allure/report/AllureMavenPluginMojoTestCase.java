package ru.yandex.qatools.allure.report;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;
import java.io.InputStream;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.12.13
 */
public class AllureMavenPluginMojoTestCase extends AbstractMojoTestCase {

    static {
        //TODO dirty hack??
//        System.setProperty("basedir", new File("allure-report/allure-maven-plugin").getAbsolutePath());
    }

    public void testSimple() throws Exception {

//        File pluginConfig = new File(getBasedir(), "src/test/resources/unit/plugin-config.xml");
//        System.out.println(pluginConfig.exists());
//        File pluginConfig = FileUtils.toFile(getClassLoader().getResource("unit/plugin-config.xml"));
//        AllureMavenPlugin mojo = (AllureMavenPlugin)super.lookupMojo("allure-maven-plugin", pluginConfig);

//        assertNotNull(mojo);
    }
}
