package ru.yandex.qatools.allure.data.plugins

import com.google.inject.AbstractModule
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
    void shouldInjectMembersToPlugins() {
        def plugin = new SomePluginWithInjection()
        def loader = [loadPlugins: { [plugin] }] as PluginLoader
        def injectable = new SomeInjectable(value: "some nice value")
        def injector = new SomeInjector(injectable: injectable)
        //noinspection GroovyUnusedAssignment
        def manager = new DefaultPluginsIndex(loader, Guice.createInjector(injector))

        plugin.injectable == injectable
    }

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

    class SomeInjector extends AbstractModule {

        SomeInjectable injectable;

        @Override
        protected void configure() {
            bind(SomeInjectable).toInstance(injectable)
        }
    }

    class SomePluginWithInjection extends SomePlugin implements PreparePlugin<SomeObject> {

        @Inject
        SomeInjectable injectable;

        @Override
        void prepare(SomeObject data) {
            //do nothing
        }
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
