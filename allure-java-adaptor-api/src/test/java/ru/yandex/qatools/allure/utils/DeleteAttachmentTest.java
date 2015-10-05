package ru.yandex.qatools.allure.utils;

import org.apache.commons.io.Charsets;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.DummyConfig;
import ru.yandex.qatools.allure.model.Attachment;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static ru.yandex.qatools.allure.utils.DirectoryMatcher.contains;
import static ru.yandex.qatools.allure.utils.DirectoryMatcher.notContains;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 30.04.14
 */
public class DeleteAttachmentTest {

    private static final String ATTACHMENT = "simple attachment context";

    private static final String TITLE = "title";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File resultsDirectory;

    private AllureResultsHelper resultsHelper;

    @Before
    public void setUp() throws Exception {
        resultsDirectory = folder.newFolder();
        resultsHelper = new AllureResultsHelper(new DummyConfig(resultsDirectory));
    }

    @Test
    public void saveAndDeleteTest() throws Exception {
        Attachment first = save(ATTACHMENT);

        assertNotNull(first);
        String firstSource = first.getSource();
        assertNotNull(firstSource);

        assertThat(resultsDirectory, contains(firstSource));

        resultsHelper.deleteAttachment(first);

        assertThat(resultsDirectory, notContains(firstSource));
    }

    public Attachment save(String string) throws IOException {
        return resultsHelper.writeAttachment(string.getBytes(Charsets.UTF_8), TITLE);
    }

}
