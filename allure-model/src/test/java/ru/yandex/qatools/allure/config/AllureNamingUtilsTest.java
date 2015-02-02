package ru.yandex.qatools.allure.config;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 02.02.15
 */
public class AllureNamingUtilsTest {

    @Test(expected = IllegalStateException.class)
    public void initTest() throws Exception {
        new AllureNamingUtils();
    }

    @Test
    public void shouldGenerateSuiteName() throws Exception {
        String name = AllureNamingUtils.generateTestSuiteFileName();
        assertNotNull(name);
        assertTrue(name.matches(new AllureConfig().getTestSuiteFileRegex()));
    }
}
