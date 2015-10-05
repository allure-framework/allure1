package ru.yandex.qatools.allure;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.JUnitCore;
import ru.yandex.qatools.allure.junit.AllureRunListener;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 28.01.15
 */
public abstract class BasicListenerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public Path resultsDirectory;

    @Before
    public void setUp() throws Exception {
        resultsDirectory = folder.newFolder().toPath();
        JUnitCore core = new JUnitCore();
        AllureRunListener listener = new AllureRunListener();
        listener.setLifecycle(new Allure(new AllureConfig() {
            @Override
            public String getRemoveAttachmentsPattern() {
                return "a^";
            }

            @Override
            public Path getResultsDirectory() {
                return resultsDirectory;
            }

            @Override
            public Charset getAttachmentsEncoding() {
                return StandardCharsets.UTF_8;
            }

            @Override
            public int getMaxTitleLength() {
                return 120;
            }
        }));
        core.addListener(listener);
        core.run(getTestClass());
    }

    public abstract Class<?> getTestClass();
}
