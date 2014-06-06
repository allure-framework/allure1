package ru.yandex.qatools.allure.experimental;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.net.URLClassLoader;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 04.06.14
 */
public class ListenersNotifierSPITest {

    private ListenersNotifier notifier;

    @Before
    public void setUp() throws Exception {
        URL url = getClass().getClassLoader().getResource("testdata/");
        URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, Thread.currentThread().getContextClassLoader());

        notifier = new ListenersNotifier(classLoader);
    }

    @Test
    public void listenersCountTest() throws Exception {
        assertThat(notifier.getListeners(), hasSize(1));
    }
}
