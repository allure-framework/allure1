package ru.yandex.qatools.allure.data.plugins

import com.google.inject.AbstractModule
import com.google.inject.Guice
import com.google.inject.Inject
import groovy.transform.EqualsAndHashCode
import org.apache.commons.io.FilenameUtils
import org.junit.ClassRule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.yandex.qatools.allure.data.WidgetType
import ru.yandex.qatools.allure.data.io.ReportWriter
import ru.yandex.qatools.allure.data.testdata.SomePluginWithResources

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 07.02.15
 */
class PluginManagerTest {

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    @Test
    void shouldNotFailIfLoadNull() {
        def loader = [loadPlugins: { null }] as PluginLoader
        new PluginManager(loader)
    }

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
        def loader = [loadPlugins: { [new SomePreparePlugin()] }] as PluginLoader
        def manager = new PluginManager(loader)

        Integer number = 4;
        manager.prepare(number)

        assert number == 4
    }

    @Test
    void shouldChangePreparedObjects() {
        def plugin1 = new SomePreparePlugin(suffix: "_PLUGIN1")
        def plugin2 = new SomePreparePlugin(suffix: "_PLUGIN2")
        def loader = [loadPlugins: { [plugin1, plugin2] }] as PluginLoader
        def manager = new PluginManager(loader)

        def object1 = new SomeObject(someValue: "object1")
        manager.prepare(object1)
        assert object1.someValue == "object1_PLUGIN1_PLUGIN2"

        def object2 = new SomeObject(someValue: "object2")
        manager.prepare(object2)
        assert object2.someValue == "object2_PLUGIN1_PLUGIN2"
    }

    @Test
    void shouldNotChangeProcessedObjects() {
        def plugin1 = new SomeProcessPlugin(suffix: "_PLUGIN1")
        def plugin2 = new SomeProcessPlugin(suffix: "_PLUGIN2")
        def loader = [loadPlugins: { [plugin1, plugin2] }] as PluginLoader
        def manager = new PluginManager(loader)

        def object1 = new SomeObject(someValue: "object1")
        manager.process(object1)
        assert object1.someValue == "object1"

        def object2 = new SomeObject(someValue: "object2")
        manager.process(object2)
        assert object2.someValue == "object2"
    }

    @Test
    void shouldUpdateDataWhenProcessObjects() {
        def plugin1 = new SomeProcessPlugin(suffix: "_PLUGIN1")
        def plugin2 = new SomeProcessPlugin(suffix: "_PLUGIN2")
        def loader = [loadPlugins: { [plugin1, plugin2] }] as PluginLoader
        def manager = new PluginManager(loader)

        def object1 = new SomeObject(someValue: "object1")
        manager.process(object1)
        def object2 = new SomeObject(someValue: "object2")
        manager.process(object2)

        def data = manager.getData(SomeObject)
        assert data
        assert data.size() == 4
        assert data.collect { item -> (item.data as SomeObject).someValue }.containsAll([
                "object1_PLUGIN1",
                "object1_PLUGIN2",
                "object2_PLUGIN1",
                "object2_PLUGIN2"
        ])
        assert data.collect { item -> item.name }.containsAll([
                "name_PLUGIN1",
                "name_PLUGIN2",
                "name_PLUGIN1",
                "name_PLUGIN2"
        ])
    }

    @Test
    void shouldInjectMembersToPlugins() {
        def plugin = new SomePluginWithInjection()
        def loader = [loadPlugins: { [plugin] }] as PluginLoader
        def injectable = new SomeInjectable(value: "some nice value")
        def injector = new SomeInjector(injectable: injectable)
        //noinspection GroovyUnusedAssignment
        def manager = new PluginManager(loader, Guice.createInjector(injector))

        plugin.injectable == injectable
    }

    @Test
    void shouldNotFailIfNullData() {
        def loader = [loadPlugins: { [new SomeProcessPluginWithNullData()] }] as PluginLoader
        def manager = new PluginManager(loader)

        manager.process(new SomeObject())
        manager.getData(null)
        assert manager.getData(SomeObject) == [null] as List<PluginData>
    }

    @Test
    void shouldCopyPluginResources() {
        def plugin = new SomePluginWithResources()
        def loader = [loadPlugins: { [plugin] }] as PluginLoader
        def manager = new PluginManager(loader)
        def writer = new DummyReportWriter(folder.newFolder())
        manager.writePluginResources(writer)

        assert writer.writtenResources.size() == 1

        def pluginResources = writer.writtenResources["somePluginWithResources"]
        assert pluginResources
        assert pluginResources.size() == 2
        assert pluginResources.containsAll(["a.txt", "b.xml"])
    }

    @Test
    void shouldWriteListOfPluginWithResources() {
        def plugin1 = new SomePluginWithResources()
        def plugin2 = new SomeProcessPlugin()
        def loader = [loadPlugins: { [plugin1, plugin2] }] as PluginLoader
        def manager = new PluginManager(loader)
        def writer = new DummyReportWriter(folder.newFolder())

        manager.writePluginList(writer)

        assert writer.writtenData.size() == 1
        assert writer.writtenData.containsKey(PluginManager.PLUGINS_JSON)

        def object = writer.writtenData.get(PluginManager.PLUGINS_JSON)
        assert object instanceof List<String>
        assert object.size() == 1
        assert object.contains("somePluginWithResources")
    }

    @Test
    void shouldWritePluginWidget() {
        def plugin1 = new SomePluginWithWidget()
        def plugin2 = new SomeProcessPlugin()
        def loader = [loadPlugins: { [plugin1, plugin2] }] as PluginLoader
        def manager = new PluginManager(loader)
        def writer = new DummyReportWriter(folder.newFolder())

        manager.writePluginWidgets(writer)

        assert writer.writtenData.size() == 1
        assert writer.writtenData.containsKey(PluginManager.WIDGETS_JSON)

        def object = writer.writtenData.get(PluginManager.WIDGETS_JSON)
        assert object instanceof List<Widget>
        assert object.size() == 1

        def widget = object.iterator().next()
        assert widget.name == "name"
        assert widget.type == WidgetType.TITLE_STATISTICS
    }

    /**
     * Should use mock instead this class, but groovy mocks suck sometimes =(
     */
    class DummyReportWriter extends ReportWriter {
        Map<String, List<String>> writtenResources = [:].withDefault {[]}
        Map<String, Object> writtenData = [:].withDefault {[]}

        DummyReportWriter(File dir) {
            super(dir)
        }

        @Override
        void write(PluginData data) {
            assert !writtenData.containsKey(data.name)
            writtenData.put(data.name, data.data)
        }

        @Override
        void write(String pluginName, URL resource) {
            writtenResources.get(pluginName).add(FilenameUtils.getName(resource.toString()))
        }
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

    class SomePluginWithWidget extends SomeProcessPlugin implements WithWidget {

        @Override
        Widget getWidget() {
            return new StatsWidget("name")
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

    class SomePreparePlugin extends SomePlugin implements PreparePlugin<SomeObject> {
        def suffix = "_SUFFIX";

        @Override
        void prepare(SomeObject data) {
            data.someValue += suffix
        }
    }

    class SomeProcessPlugin extends SomePlugin implements ProcessPlugin<SomeObject> {
        def suffix = "_SUFFIX"
        List<PluginData> pluginData = []

        @Override
        void process(SomeObject data) {
            data.someValue += suffix
            pluginData.add(new PluginData("name" + suffix, data))
        }

        @Override
        List<PluginData> getPluginData() {
            return pluginData
        }
    }

    class SomeProcessPluginWithNullData extends SomePlugin implements ProcessPlugin<SomeObject> {

        @Override
        void process(SomeObject data) {
            //do nothing
        }

        @Override
        List<PluginData> getPluginData() {
            return null
        }
    }

    abstract class SomePlugin implements Plugin<SomeObject> {
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
