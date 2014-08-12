package ru.yandex.qatools.allure.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 28.05.14
 */
public class AllureResultsUtilsTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test(expected = IllegalStateException.class)
    public void createUtilsTest() throws Exception {
        new AllureResultsUtils();
    }

    @Test
    public void createDirectoriesDoesntExistsTest() throws Exception {
        File directory = new File(folder.getRoot(), "a");

        assertTrue(AllureResultsUtils.createDirectories(directory));
    }

    @Test
    public void createDirectoriesExistsTest() throws Exception {
        assertTrue(AllureResultsUtils.createDirectories(folder.newFolder()));
    }

    @Test
    public void getExtensionByValidMimeTypeTest() throws Exception {
        assertThat(AllureResultsUtils.getExtensionByMimeType("text/plain"), is(".txt"));
    }

    @Test
    public void getExtensionByInvalidMimeTypeTest() throws Exception {
        assertThat(AllureResultsUtils.getExtensionByMimeType("text/plai"), is(""));
    }
}
