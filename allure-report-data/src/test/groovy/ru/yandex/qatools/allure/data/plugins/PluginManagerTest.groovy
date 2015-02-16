package ru.yandex.qatools.allure.data.plugins

import groovy.transform.EqualsAndHashCode
import org.junit.Ignore
import org.junit.Test

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 07.02.15
 */
class PluginManagerTest {

    @Ignore("Not implemented yet")
    @Test
    void shouldNotFailIfProcessNull() {

        def loader = [loadPlugins: { [] }] as PluginLoader
        def manager = new PluginManager(loader)

        manager.prepare(null)
        manager.process(null)
        manager.getData(null);
    }

    @Test
    void shouldNotFailIfNoPlugins() {

        def loader = [loadPlugins: { [] }] as PluginLoader
        def manager = new PluginManager(loader)

        manager.prepare(new Object())
        manager.process(new ArrayList())
        manager.getData(Integer);
    }

    @Ignore("Not implemented yet")
    @Test
    void shouldNotFailIfNullPlugins() {

        def loader = [loadPlugins: { [null] }] as PluginLoader
        def manager = new PluginManager(loader)

        manager.prepare(new Object())
        manager.process(new ArrayList())
        manager.getData(Integer);
    }

    @Test
    void shouldDoNothingIfNoPluginForTypePreparedObject() {
        def loader = [loadPlugins: { [new SomePlugin()] }] as PluginLoader
        def manager = new PluginManager(loader)

        Integer number = 4;
        manager.prepare(number)

        assert number == 4
    }

    @Test
    void shouldChangeBothPreparedObject() {
        def plugin = new SomePlugin();
        def loader = [loadPlugins: { [plugin] }] as PluginLoader
        def manager = new PluginManager(loader)

        def object1 = new SomeObject(someValue: "object1")
        manager.prepare(object1)
        assert object1.someValue == "object1_SUFFIX"

        def object2 = new SomeObject(someValue: "object2")
        manager.prepare(object2)
        assert object2.someValue == "object2_SUFFIX"
    }

    @Test
    void shouldChangePreparedObjectFewPlugins() {
        def plugin1 = new SomePlugin(suffix: "_PLUGIN1")
        def plugin2 = new SomePlugin(suffix: "_PLUGIN2")
        def loader = [loadPlugins: { [plugin1, plugin2] }] as PluginLoader
        def manager = new PluginManager(loader)

        def object1 = new SomeObject(someValue: "object1")
        manager.prepare(object1)
        assert object1.someValue == "object1_PLUGIN1_PLUGIN2"

        def object2 = new SomeObject(someValue: "object2")
        manager.prepare(object2)
        assert object2.someValue == "object2_PLUGIN1_PLUGIN2"
    }


    class SomePlugin implements PreparePlugin<SomeObject> {
        def suffix = "_SUFFIX";

        @Override
        void prepare(SomeObject data) {
            data.someValue += suffix
        }

        @Override
        Class<SomeObject> getType() {
            return SomeObject
        }
    }

    @EqualsAndHashCode
    class SomeObject {
        String someValue;
    }
}
