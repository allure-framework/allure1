package ru.yandex.qatools.allure.config;

import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 23.05.14
 */
public class VersionTest {
    @Test
    public void versionTest() throws Exception {
        assertThat(
                AllureConfig.newInstance().getVersion(),
                is(System.getProperty("project.version"))
        );
    }
}
