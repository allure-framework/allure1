package ru.yandex.qatools.allure.commons;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileFilter;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static ru.yandex.qatools.allure.commons.AllureFileUtils.listAttachmentFiles;
import static ru.yandex.qatools.allure.commons.AllureFileUtils.listTestSuiteFiles;
import static ru.yandex.qatools.allure.config.AllureNamingUtils.generateTestSuiteFileName;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.06.14
 */
public class AllureFileUtilsTest {

    private File directory;

    @Before
    public void initDirectory() {
        directory = mock(File.class);
        when(directory.isDirectory()).thenReturn(true);
        when(directory.canRead()).thenReturn(true);
    }

    @Test(expected = IllegalStateException.class)
    public void createInstanceTest() throws Exception {
        new AllureFileUtils();
    }

    @Test
    public void generatedTestSuiteFileNamesMustBeFoundByListTestSuiteFilesMethod() {
        File[] testSuiteFiles = new File[]{
                new File(generateTestSuiteFileName()),
                new File(generateTestSuiteFileName())
        };

        when(directory.listFiles(any(FileFilter.class))).thenReturn(testSuiteFiles);
        assertThat(listTestSuiteFiles(directory).size(), equalTo(testSuiteFiles.length));
    }

    @Test
    public void ListAttachmentFilesTest() {
        File[] attachmentFiles = new File[]{
                new File("a-attachment"),
                new File("b-attachment.xml")
        };

        when(directory.listFiles(any(FileFilter.class))).thenReturn(attachmentFiles);
        assertThat(listAttachmentFiles(directory).size(), equalTo(attachmentFiles.length));
    }
}
