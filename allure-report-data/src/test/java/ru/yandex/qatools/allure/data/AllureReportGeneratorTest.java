package ru.yandex.qatools.allure.data;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.data.io.ReportWriter;
import ru.yandex.qatools.allure.data.plugins.PluginManager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static ru.yandex.qatools.matchers.nio.PathMatchers.contains;
import static ru.yandex.qatools.matchers.nio.PathMatchers.hasFilesCount;

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
        generator.generate(outputDirectory);

        Path dataDirectory = outputDirectory.toPath().resolve(ReportWriter.DATA_DIRECTORY_NAME);
        assertTrue("Data directory should be created", Files.exists(dataDirectory));

        assertThat(dataDirectory, contains("xunit.json"));
        assertThat(dataDirectory, contains("timeline.json"));
        assertThat(dataDirectory, contains("behaviors.json"));
        assertThat(dataDirectory, contains("defects.json"));
        assertThat(dataDirectory, contains("graph.json"));
        assertThat(dataDirectory, contains(PluginManager.WIDGETS_JSON));
        assertThat(dataDirectory, contains(ReportWriter.REPORT_JSON));

        assertThat(dataDirectory, hasFilesCount(15, "*-attachment*"));
        assertThat(dataDirectory, hasFilesCount(321, "*-testcase.json"));
    }

    @Test(expected = ReportGenerationException.class)
    public void shouldFailIfNoResults() throws Exception {
        AllureReportGenerator generator = new AllureReportGenerator(folder.newFolder());
        generator.generate(folder.newFolder());
    }


    @Test(expected = ReportGenerationException.class)
    public void shouldFailIfNoResultsDirectory() throws Exception {
        AllureReportGenerator generator = new AllureReportGenerator(
                new File(folder.newFolder(), "unknown-directory"));
        generator.generate(folder.newFolder());
    }
}
