package ru.yandex.qatools.allure.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.data.io.ReportWriter;
import ru.yandex.qatools.allure.data.plugins.PluginManager;

import java.io.File;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import static ru.yandex.qatools.allure.commons.AllureFileUtils.listAttachmentFiles;
import static ru.yandex.qatools.allure.commons.AllureFileUtils.listFilesByRegex;
import static ru.yandex.qatools.allure.data.utils.DirectoryMatcher.contains;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 19.02.15
 */
public class AllureReportGeneratorTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();


    @Test
    public void shouldGenerateWithoutFailures() throws Exception {
        AllureReportGenerator generator = new AllureReportGenerator(new File("target/allure-results"));
        File outputDirectory = folder.newFolder();
        File[] listBefore = outputDirectory.listFiles();
        assumeTrue("Output directory must be empty ", listBefore != null && listBefore.length == 0);
        generator.generate(outputDirectory);

        File dataDirectory = new File(outputDirectory, ReportWriter.DATA_DIRECTORY_NAME);
        assertTrue("Data directory should be created", dataDirectory.exists());

        assertThat(dataDirectory, contains("xunit.json"));
        assertThat(dataDirectory, contains("timeline.json"));
        assertThat(dataDirectory, contains("behaviors.json"));
        assertThat(dataDirectory, contains("defects.json"));
        assertThat(dataDirectory, contains("environment.json"));
        assertThat(dataDirectory, contains("graph.json"));
        assertThat(dataDirectory, contains(PluginManager.WIDGETS_JSON));
        assertThat(dataDirectory, contains(ReportWriter.REPORT_JSON));

        assertThat(listAttachmentFiles(dataDirectory), not(empty()));
        assertThat(listFilesByRegex(".+-testcase\\.json", dataDirectory), not(empty()));
    }

    @Test(expected = ReportGenerationException.class)
    public void shouldFailIfNoResults() throws Exception {
        AllureReportGenerator generator = new AllureReportGenerator(folder.newFolder());
        generator.generate(folder.newFolder());
    }


    @Test(expected = ReportGenerationException.class)
    public void shouldFailIfNoResultsDirectory() throws Exception {
        AllureReportGenerator generator = new AllureReportGenerator(new File("unknown-directory"));
        generator.generate(folder.newFolder());
    }
}
