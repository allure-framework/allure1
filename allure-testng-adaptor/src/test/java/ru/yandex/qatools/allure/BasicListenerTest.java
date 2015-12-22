package ru.yandex.qatools.allure;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.testng.TestNG;
import ru.yandex.qatools.allure.testng.AllureTestListener;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 28.01.15
 */
public abstract class BasicListenerTest {

    public static final String SUITE1 = "suite1.xml";
    public static final String SUITE2 = "suite2.xml";
    public static final String SUITE3 = "suite3.xml";

    private static final String DEFAULT_SUITE_NAME = "suite";

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public Path resultsDirectory;

    @Before
    public void setUp() throws Exception {
        resultsDirectory = folder.newFolder().toPath();
        AllureTestListener listener = new AllureTestListener();
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


        TestNG testNG = new TestNG(false);
        testNG.setDefaultSuiteName(DEFAULT_SUITE_NAME);
        testNG.setServiceLoaderClassLoader(null);
        testNG.addListener((Object) listener);

        configure(testNG);

        testNG.run();
    }

    protected String getResourcePath(String resourceName) throws URISyntaxException {
        URL resource = getClass().getClassLoader().getResource(resourceName);
        assertThat("Could not find resource " + resourceName, resource, notNullValue());
        return Paths.get(resource.toURI()).toAbsolutePath().toString();
    }

    public abstract void configure(TestNG testNG) throws Exception;
}
