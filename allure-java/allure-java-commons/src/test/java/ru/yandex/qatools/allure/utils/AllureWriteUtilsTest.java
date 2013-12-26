package ru.yandex.qatools.allure.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.config.AllureResultsConfig;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/26/13
 */
public class AllureWriteUtilsTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void copyAttachmentTest() {
        System.out.println(ClassLoader.getSystemResource(AllureResultsConfig.newInstance().getDirectoryPath()));
    }
}
