package ru.yandex.qatools.allure.utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import ru.yandex.qatools.allure.DummyConfig;

import java.io.File;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static ru.yandex.qatools.allure.utils.AllureResultsHelper.getExtensionByMimeType;

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
}
