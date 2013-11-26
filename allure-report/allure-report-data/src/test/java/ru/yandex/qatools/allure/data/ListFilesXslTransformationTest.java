package ru.yandex.qatools.allure.data;

import org.junit.Test;

import java.io.File;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 18.11.13
 */
public class ListFilesXslTransformationTest {

    @Test
    public void notEmptyListFilesTest() throws Exception {
        TestSuitesPack testSuitesPack = getTestSuitesPack(
                getFileFromResources("testdata3")
        );

        assertThat(testSuitesPack.getTestSuites(), is(notNullValue()));
        assertThat(testSuitesPack.getTestSuites().size(), is(2));
        checkTime(testSuitesPack.getTime(), Long.valueOf("1384780017376"), Long.valueOf("1384780017556"), 180);
    }

    @Test
    public void emptyListFilesTest() throws Exception {
        TestSuitesPack testSuitesPack = getTestSuitesPack(
                getFileFromResources("testdata4")
        );

        assertThat(testSuitesPack.getTestSuites(), is(notNullValue()));
        assertThat(testSuitesPack.getTestSuites().size(), is(0));
        checkTime(testSuitesPack.getTime(), 0, 0, 0);
    }

    private static File getFileFromResources(String name) {
        return new File(ClassLoader.getSystemResource(name).getFile());
    }

    private static TestSuitesPack getTestSuitesPack(File... dirs) {
        ListFiles listFiles = AllureReportGenerator.createListFiles(dirs);
        return AllureReportGenerator.createTestSuitesPack(listFiles);
    }

    private static void checkTime(Time time, long start, long stop, long duration) {
        assertThat(time, is(notNullValue()));
        assertThat(time.getStart(), is(start));
        assertThat(time.getStop(), is(stop));
        assertThat(time.getDuration(), is(duration));
    }
}
