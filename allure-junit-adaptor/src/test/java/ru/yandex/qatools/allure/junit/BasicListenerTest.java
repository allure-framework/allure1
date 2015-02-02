package ru.yandex.qatools.allure.junit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.JUnitCore;
import ru.yandex.qatools.allure.utils.AllureResultsUtils;

import java.io.File;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 28.01.15
 */
public abstract class BasicListenerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public File resultsDirectory;

    @Before
    public void setUp() throws Exception {
        resultsDirectory = folder.newFolder();
        AllureResultsUtils.setResultsDirectory(resultsDirectory);

        JUnitCore core = new JUnitCore();
        core.addListener(new AllureRunListener());
        core.run(getTestClass());
    }

    @After
    public void tearDown() {
        AllureResultsUtils.setResultsDirectory(null);
    }

    public abstract Class<?> getTestClass();
}
