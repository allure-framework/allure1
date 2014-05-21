package ru.yandex.qatools.allure.utils;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.qatools.allure.model.Attachment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static ru.yandex.qatools.allure.utils.DirectoryMatcher.contains;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 21.05.14
 */
@RunWith(Parameterized.class)
public class WriteAttachmentTest {

    private static final String TITLE = "some.title";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private File resultsDirectory;

    private String type;

    private byte[] bytes;

    public WriteAttachmentTest(String resourceName, String type) throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream(resourceName);
        this.bytes = IOUtils.toByteArray(is);
        this.type = type;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{"a.html", "text/html"},
                new Object[]{"a.jpeg", "image/jpeg"},
                //JSON doesn't have metadata about content type in first bytes
                new Object[]{"a.json", "text/plain"},
                new Object[]{"a.png", "image/png"},
                new Object[]{"a.txt", "text/plain"},
                new Object[]{"a.xml", "application/xml"}
        );
    }

    @Before
    public void setUp() throws Exception {
        resultsDirectory = folder.newFolder();
        AllureResultsUtils.setResultsDirectory(resultsDirectory);
    }

    @Test
    public void typeTest() throws Exception {
        Attachment attachment = AllureResultsUtils.writeAttachment(bytes, TITLE);
        assertThat(attachment.getTitle(), is(TITLE));
        assertThat(resultsDirectory, contains(attachment.getSource()));
        assertThat(attachment.getType(), is(type));
    }
}
