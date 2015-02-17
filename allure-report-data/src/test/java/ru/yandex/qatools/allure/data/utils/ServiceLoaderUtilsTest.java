package ru.yandex.qatools.allure.data.utils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.yandex.qatools.allure.data.testdata.SomeService;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 18.02.15
 */
@RunWith(Parameterized.class)
public class ServiceLoaderUtilsTest {

    private String resourceDirectory;

    private int countOfServicesShouldBeLoaded;

    private ClassLoader classLoader;

    public ServiceLoaderUtilsTest(String resourceDirectory, int countOfServicesShouldBeLoaded) {
        this.resourceDirectory = resourceDirectory;
        this.countOfServicesShouldBeLoaded = countOfServicesShouldBeLoaded;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(
                new Object[]{"testdata1/", 0},
                new Object[]{"testdata2/", 0},
                new Object[]{"testdata3/", 1}
        );
    }

    @Before
    public void setUp() {
        URL url = getClass().getClassLoader().getResource(resourceDirectory);
        classLoader = new URLClassLoader(
                new URL[]{url},
                Thread.currentThread().getContextClassLoader()
        );
    }

    @Test
    public void shouldNotFail() throws Exception {
        List<SomeService> loaded = ServiceLoaderUtils.load(classLoader, SomeService.class);
        assertNotNull(loaded);
        assertThat(loaded, hasSize(countOfServicesShouldBeLoaded));
    }
}
