package ru.yandex.qatools.allure.events;

import org.apache.commons.io.Charsets;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.Step;
import ru.yandex.qatools.allure.utils.AllureResultsUtils;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static ru.yandex.qatools.allure.utils.AllureResultsUtils.writeAttachment;
import static ru.yandex.qatools.allure.utils.DirectoryMatcher.contains;
import static ru.yandex.qatools.allure.utils.DirectoryMatcher.notContains;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 05.05.14
 */
public class RemoveAttachmentsEventTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private Step root = new Step();

    private File resultsDirectory;

    @Before
    public void setUp() throws Exception {
        resultsDirectory = folder.newFolder();
        AllureResultsUtils.setResultsDirectory(resultsDirectory);

        Step child1 = new Step().withName("child1");
        Step child2 = new Step().withName("child2");

        child1.getAttachments().add(save("firstAttach"));
        child2.getAttachments().add(save("secondAttach"));

        root.getAttachments().add(save("rootAttach"));
        root.getSteps().add(child1);
        root.getSteps().add(child2);
    }

    @Test
    public void shouldNotDeleteAnyByDefault() throws Exception {
        String[] files = resultsDirectory.list();

        new RemoveAttachmentsEvent("a^").process(root);

        for (String file : files) {
            assertThat(resultsDirectory, contains(file));
        }

        checkAttachmentsCount(root, 1);
    }

    @Test
    public void removeAllAttachmentsTest() throws Exception {
        String[] files = resultsDirectory.list();

        new RemoveAttachmentsEvent(".*").process(root);

        for (String file : files) {
            assertThat(resultsDirectory, notContains(file));
        }

        checkAttachmentsCount(root, 0);
    }

    public void checkAttachmentsCount(Step root, int count) {
        assertThat(count, is(root.getAttachments().size()));
        for (Step step : root.getSteps()) {
            checkAttachmentsCount(step, count);
        }
    }

    public Attachment save(String string) throws IOException {
        return writeAttachment(string.getBytes(Charsets.UTF_8), "other-title");
    }

}
