package ru.yandex.qatools.allure.utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.AttachmentType;

import java.io.File;

import static org.junit.Assert.*;
import static ru.yandex.qatools.allure.utils.AllureResultsUtils.deleteAttachment;
import static ru.yandex.qatools.allure.utils.AllureResultsUtils.writeAttachment;
import static ru.yandex.qatools.allure.utils.DirectoryMatcher.contains;
import static ru.yandex.qatools.allure.utils.DirectoryMatcher.notContains;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 30.04.14
 */
public class SaveAttachmentTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public File resultsDirectory;

    private static final String FILE = "simple-file-attachment.txt";

    private static final String STRING = "simple attachment context";

    @Before
    public void setUp() throws Exception {
        resultsDirectory = folder.newFolder();
        AllureResultsUtils.setResultsDirectory(resultsDirectory);
    }

    @Test
    public void saveAndDeleteTest() throws Exception {
        File file = getResourceAsFile(FILE);
        Attachment first = save(file);
        assertNotNull(first);
        String firstSource = first.getSource();
        assertNotNull(firstSource);

        assertThat(resultsDirectory, contains(firstSource));

        Attachment second = save(STRING);
        assertNotNull(second);
        String secondSource = second.getSource();
        assertNotNull(secondSource);

        assertEquals(firstSource, secondSource);

        assertThat(resultsDirectory, contains(secondSource));

        deleteAttachment(first);

        assertThat(resultsDirectory, notContains(firstSource));
        assertThat(resultsDirectory, notContains(secondSource));

        deleteAttachment(second);

        assertThat(resultsDirectory, notContains(firstSource));
        assertThat(resultsDirectory, notContains(secondSource));
    }

    public Attachment save(File file) {
        String source = writeAttachment(file, AttachmentType.TXT);

        return new Attachment().withSource(source).withType(AttachmentType.TXT).withTitle("some-title");
    }

    public Attachment save(String string) {
        String source = writeAttachment(string, AttachmentType.TXT);

        return new Attachment().withSource(source).withType(AttachmentType.TXT).withTitle("other-title");
    }

    public File getResourceAsFile(String resourcePath) throws Exception {
        return new File(getClass().getClassLoader().getResource(resourcePath).toURI());
    }


}
