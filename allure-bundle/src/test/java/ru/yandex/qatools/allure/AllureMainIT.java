package ru.yandex.qatools.allure;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.MatcherAssert.assertThat;
import static ru.yandex.qatools.allure.AllureMain.main;
import static ru.yandex.qatools.matchers.nio.PathMatchers.contains;
import static ru.yandex.qatools.matchers.nio.PathMatchers.exists;
import static ru.yandex.qatools.matchers.nio.PathMatchers.hasFilesCount;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.10.15
 */
public class AllureMainIT {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Path resultsDirectory;

    private Path output;

    @Before
    public void setUp() throws Exception {
        resultsDirectory = Paths.get("target/allure-results");
        output = folder.newFolder().toPath();

        main(new String[]{
                resultsDirectory.toAbsolutePath().toString(),
                output.toAbsolutePath().toString()
        });
    }

    @Test
    public void shouldGenerateData() throws Exception {
        Path data = output.resolve("data");
        assertThat(data, exists());
        assertThat(data, hasFilesCount(320, "*-testcase.json"));
        assertThat(data, contains("xunit.json"));
    }

    @Test
    public void shouldGenerateIndex() throws Exception {
        Path index = output.resolve("index.html");
        assertThat(index, exists());
    }

    @Test
    public void shouldCopyResources() throws Exception {
        assertThat(output.resolve("app.js"), exists());
        assertThat(output.resolve("styles.css"), exists());
    }

    @Test
    public void shouldOverrideResources() throws Exception {
        main(new String[]{
                resultsDirectory.toAbsolutePath().toString(),
                output.toAbsolutePath().toString()
        });
    }
}
