package ru.yandex.qatools.allure.data.plugins

import com.google.inject.Guice
import com.google.inject.Inject
import groovy.transform.EqualsAndHashCode
import org.junit.Test
import ru.yandex.qatools.allure.data.AllureGuiceModule

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.07.15
 */
class PluginsIndexTest {

    @Test
    public void shouldBeSingleton() throws Exception {
        def injector = Guice.createInjector(new AllureGuiceModule(getClass().classLoader))

        def first = new SomeClassWithIndexInject()
        def second = new SomeClassWithIndexInject()
        injector.injectMembers(first)
        injector.injectMembers(second)

        assert first.index == second.index
    }

    class SomeClassWithIndexInject {

        @Inject
        PluginsIndex index
    }

    @EqualsAndHashCode
    class SomeInjectable {
        String value
    }

    abstract class SomePlugin {
        Class<SomeObject> getType() {
            return SomeObject
        }
    }

    @EqualsAndHashCode
    class SomeObject {
        String someValue;
    }
}
