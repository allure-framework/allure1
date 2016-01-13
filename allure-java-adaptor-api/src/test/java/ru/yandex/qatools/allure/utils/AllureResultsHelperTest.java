package ru.yandex.qatools.allure.utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.DummyConfig;
import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.Step;

import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static ru.yandex.qatools.allure.utils.AllureResultsHelper.getExtensionByMimeType;
import static ru.yandex.qatools.allure.utils.testdata.TestData.randomAttachment;
import static ru.yandex.qatools.allure.utils.testdata.TestData.randomStep;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 28.05.14
 */
public class AllureResultsHelperTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private AllureResultsHelper helper;

    @Before
    public void setUp() throws Exception {
        helper = new AllureResultsHelper(new DummyConfig(folder.newFolder()));
    }

    @Test
    public void getExtensionByValidMimeTypeTest() throws Exception {
        assertThat(getExtensionByMimeType("text/plain"), is(".txt"));
    }

    @Test
    public void getExtensionByInvalidMimeTypeTest() throws Exception {
        assertThat(getExtensionByMimeType("text/plai"), is(""));
    }

    @Test
    public void shouldDeleteAttachments() throws Exception {
        Step first = randomStep();
        Step second = randomStep();

        Attachment firstPng = randomAttachment("a.png");
        Attachment secondPng = randomAttachment("b.png");
        Attachment txt = randomAttachment("c.txt");

        first.withAttachments(firstPng, txt);
        second.withAttachments(secondPng);
        first.withSteps(second);

        helper.deleteAttachments(first, Pattern.compile(".*\\.png"));

        assertThat(first.getAttachments(), hasSize(1));
        assertThat(first.getAttachments(), hasItems(txt));
        assertThat(second.getAttachments(), empty());
    }
}
