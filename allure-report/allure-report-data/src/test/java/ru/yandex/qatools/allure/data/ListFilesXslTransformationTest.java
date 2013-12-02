package ru.yandex.qatools.allure.data;

import org.junit.Test;
import ru.yandex.qatools.allure.data.generators.TestSuiteFiles;

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
        TestSuiteFiles testdata3 = new TestSuiteFiles(
                getFileFromResources("testdata3")
        );

        AllureTestRun testRun = testdata3.generateTestRun().getAllureTestRun();

        assertThat(testRun.getTestSuites(), is(notNullValue()));
        assertThat(testRun.getTestSuites().size(), is(2));
        checkTime(testRun.getTime(), Long.valueOf("1384780017376"), Long.valueOf("1384780017556"), 180);
    }

    @Test
    public void emptyListFilesTest() throws Exception {
        TestSuiteFiles testdata4 = new TestSuiteFiles(
                getFileFromResources("testdata4")
        );

        AllureTestRun testRun = testdata4.generateTestRun().getAllureTestRun();

        assertThat(testRun.getTestSuites(), is(notNullValue()));
        assertThat(testRun.getTestSuites().size(), is(0));
        checkTime(testRun.getTime(), 0, 0, 0);
    }

    private static File getFileFromResources(String name) {
        return new File(ClassLoader.getSystemResource(name).getFile());
    }

    private static void checkTime(Time time, long start, long stop, long duration) {
        assertThat(time, is(notNullValue()));
        assertThat(time.getStart(), is(start));
        assertThat(time.getStop(), is(stop));
        assertThat(time.getDuration(), is(duration));
    }
}
