package ru.yandex.qatools.allure.data.plugins

import groovy.transform.EqualsAndHashCode
import org.apache.commons.io.FilenameUtils
import org.junit.ClassRule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.yandex.qatools.allure.data.Widgets
import ru.yandex.qatools.allure.data.index.DefaultPluginsIndex
import ru.yandex.qatools.allure.data.io.ReportWriter
import ru.yandex.qatools.allure.data.testdata.SomePluginWithResources

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 07.02.15
 */
class PluginManagerTest {

    @ClassRule
    public static TemporaryFolder folder = new TemporaryFolder();

    def writer = new DummyReportWriter(
            folder.newFolder()
    )

    @Test
    void shouldNotFailIfLoadNull() {
        createPluginManager(null)
    }

    @Test
    void shouldNotFailIfProcessNull() {
        def manager = createPluginManager([])

        manager.prepare(null)
        manager.process(null)
    }

    @Test
    void shouldNotFailIfNoPlugins() {
        def manager = createPluginManager([])

        manager.prepare(new Object())
        manager.process(new ArrayList())
    }

    @Test
    void shouldNotFailIfNullPlugins() {
        def manager = createPluginManager([null])

        manager.prepare(new Object())
        manager.process(new ArrayList())

        assert manager.pluginsData == [] as List<PluginData>
    }

    @Test
    void shouldDoNothingIfNoPluginForTypePreparedObject() {
        def manager = createPluginManager([new SomePreparePlugin()])

        Integer number = 4;
        manager.prepare(number)

        assert number == 4
    }

    @Test
    void shouldChangePreparedObjects() {
        def plugin1 = new SomePreparePlugin(suffix: "_PLUGIN1")
        def plugin2 = new SomePreparePlugin(suffix: "_PLUGIN2")
        def manager = createPluginManager([plugin1, plugin2])

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
        def manager = createPluginManager([plugin1, plugin2])

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
        def manager = createPluginManager([plugin1, plugin2])

        def object1 = new SomeObject(someValue: "object1")
        manager.process(object1)
        def object2 = new SomeObject(someValue: "object2")
        manager.process(object2)

        def data = manager.pluginsData
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
    void shouldNotFailIfNullData() {
        def manager = createPluginManager([new SomeProcessPluginWithNullData()])

        manager.process(new SomeObject())
        assert manager.pluginsData == [null] as List<PluginData>
    }

    @Test
    void shouldCopyPluginResources() {
        def plugin = new SomePluginWithResources()
        def manager = createPluginManager([plugin])
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
        def manager = createPluginManager([plugin1, plugin2])

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
        def manager = createPluginManager([plugin1, plugin2])

        manager.writePluginWidgets(writer)

        assert writer.writtenData.size() == 1
        assert writer.writtenData.containsKey(PluginManager.WIDGETS_JSON)

        def object = writer.writtenData.get(PluginManager.WIDGETS_JSON)

        assert object instanceof Widgets
        assert object.hash instanceof String
        assert !object.hash.empty
        assert object.plugins instanceof Map<String, Object>
        assert object.plugins.size() == 1

        def data = object.plugins.get("somePlugin") as List
        assert data.size() == 2
        assert data.contains('a')
        assert data.contains('b')
    }

    @Test
    void shouldProcessPluginsByPriority() {
        def manager = createPluginManager([
                new SomeProcessPlugin(suffix: "without_lexSecond"),
                new SomeOtherProcessPlugin(suffix: "without_lexFirst"),
                new SomePluginWithLowPriority(suffix: "with_low"),
                new SomePluginWithHighPriority(suffix: "with_high")
        ])

        manager.process(new SomeObject(someValue: "value_"))

        def data = manager.pluginsData
        assert data
        assert data.size() == 4
        assert data.collect { (it.data as SomeObject).someValue } == [
                "value_with_high",
                "value_with_low",
                "value_without_lexFirst",
                "value_without_lexSecond"
        ]
    }

    static PluginManager createPluginManager(List<Plugin> plugins) {
        def index = new DefaultPluginsIndex(plugins)
        new PluginManager(index)
    }

    /**
     * Should use mock instead this class, but groovy mocks suck sometimes =(
     */
    class DummyReportWriter extends ReportWriter {
        Map<String, List<String>> writtenResources = [:].withDefault { [] }
        Map<String, Object> writtenData = [:].withDefault { [] }

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

    class SomePluginWithHighPriority extends SomeProcessPlugin implements WithPriority {

        @Override
        int getPriority() {
            return 100
        }
    }

    class SomePluginWithLowPriority extends SomeProcessPlugin implements WithPriority {

        @Override
        int getPriority() {
            return 10
        }
    }

    class SomeOtherProcessPlugin extends SomeProcessPlugin {
    }

    class SomePluginWithWidget extends SomeProcessPlugin implements WithWidget {

        @Override
        String getName() {
            return "somePlugin";
        }

        @Override
        Object getWidgetData() {
            return ['a', 'b']
        }
    }


    class SomePreparePlugin extends SomePlugin implements PreparePlugin<SomeObject> {
        def suffix = "_SUFFIX";

        @Override
        void prepare(SomeObject data) {
            data.someValue += suffix
        }
    }

    class SomeProcessPlugin extends SomePlugin implements ProcessPlugin<SomeObject>, WithData {
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

    class SomeProcessPluginWithNullData extends SomePlugin implements ProcessPlugin<SomeObject>, WithData {

        @Override
        void process(SomeObject data) {
            //do nothing
        }

        @Override
        List<PluginData> getPluginData() {
            return null
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
