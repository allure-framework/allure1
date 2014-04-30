package ru.yandex.qatools.allure.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.NameFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.AttachmentType;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static ru.yandex.qatools.allure.utils.AllureResultsUtils.deleteAttachment;
import static ru.yandex.qatools.allure.utils.AllureResultsUtils.writeAttachment;

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

        checkDirectoryContains(firstSource, true);

        Attachment second = save(STRING);
        assertNotNull(second);
        String secondSource = second.getSource();
        assertNotNull(secondSource);

        assertEquals(firstSource, secondSource);

        checkDirectoryContains(secondSource, true);

        deleteAttachment(first);

        checkDirectoryContains(firstSource, false);
        checkDirectoryContains(secondSource, false);

        deleteAttachment(second);

        checkDirectoryContains(firstSource, false);
        checkDirectoryContains(secondSource, false);
    }

    public void checkDirectoryContains(String fileName, boolean contains) {
        int size = FileUtils.listFiles(
                resultsDirectory,
                new NameFileFilter(fileName),
                TrueFileFilter.INSTANCE
        ).size();
        assertEquals(size, contains ? 1 : 0);
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
