package ru.yandex.qatools.allure.utils;

import org.apache.commons.io.Charsets;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.DummyConfig;
import ru.yandex.qatools.allure.model.Attachment;

import java.nio.file.Path;

import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static ru.yandex.qatools.matchers.nio.PathMatchers.contains;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 30.04.14
 */
public class DeleteAttachmentTest {

    private static final String ATTACHMENT = "simple attachment context";

    private static final String TITLE = "title";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Path resultsDirectory;

    private AllureResultsHelper resultsHelper;

    @Before
    public void setUp() throws Exception {
        resultsDirectory = folder.newFolder().toPath();
        resultsHelper = new AllureResultsHelper(new DummyConfig(resultsDirectory));
    }

    @Test
    public void saveAndDeleteTest() throws Exception {
        Attachment first = resultsHelper.writeAttachment(ATTACHMENT.getBytes(Charsets.UTF_8), TITLE);

        assertNotNull(first);
        String firstSource = first.getSource();
        assertNotNull(firstSource);
        assertThat(resultsDirectory, contains(firstSource));

        resultsHelper.deleteAttachment(first);
        assertThat(resultsDirectory, not(contains(firstSource)));
    }

}
