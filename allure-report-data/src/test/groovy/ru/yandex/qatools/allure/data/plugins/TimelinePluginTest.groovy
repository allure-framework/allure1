package ru.yandex.qatools.allure.data.plugins

import org.codehaus.groovy.runtime.InvokerHelper
import org.junit.Test
import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.utils.PluginUtils

import static ru.yandex.qatools.allure.config.AllureModelUtils.createHostLabel
import static ru.yandex.qatools.allure.config.AllureModelUtils.createThreadLabel
import static ru.yandex.qatools.allure.data.utils.PluginUtils.DEFAULT_HOST
import static ru.yandex.qatools.allure.data.utils.PluginUtils.DEFAULT_THREAD

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 07.02.15
 */
class TimelinePluginTest {

    def plugin = new TimelinePlugin()

    @Test
    void shouldGroupByDefault() {
        def testCase = new AllureTestCase(uid: "uid", name: "name")

        plugin.process(testCase)

        assert plugin.timeline.hosts.size() == 1

        def host = plugin.timeline.hosts[0]
        assert host.title == DEFAULT_HOST
        assert host.threads.size() == 1

        def thread = host.threads[0]
        assert thread.title == DEFAULT_THREAD
        assert thread.testCases.size() == 1

        def info = thread.testCases[0]
        use([InvokerHelper, PluginUtils]) {
            assert info.getProperties() == testCase.toInfo().getProperties()
        }
    }

    @Test
    void shouldGroupByHostAndThread() {
        def testCase = new AllureTestCase(uid: "uid", name: "name",
                labels: [createHostLabel("host"), createThreadLabel("thread")]
        )

        plugin.process(testCase)

        assert plugin.timeline.hosts.size() == 1

        def host = plugin.timeline.hosts[0]
        assert host.title == "host"
        assert host.threads.size() == 1

        def thread = host.threads[0]
        assert thread.title == "thread"
        assert thread.testCases.size() == 1

        def info = thread.testCases[0]
        use([InvokerHelper, PluginUtils]) {
            assert info.getProperties() == testCase.toInfo().getProperties()
        }
    }

    @Test
    void shouldGroupByHostAndThreadFewTestCases() {
        def testCase1 = new AllureTestCase(uid: "uid1", name: "name1",
                labels: [createHostLabel("host1"), createThreadLabel("thread1")]
        )

        def testCase2 = new AllureTestCase(uid: "uid2", name: "nam2",
                labels: [createHostLabel("host1"), createThreadLabel("thread1")]
        )

        def testCase3 = new AllureTestCase(uid: "uid3", name: "nam3",
                labels: [createHostLabel("host1"), createThreadLabel("thread2")]
        )

        def testCase4 = new AllureTestCase(uid: "uid4", name: "nam4",
                labels: [createHostLabel("host2"), createThreadLabel("thread1")]
        )

        plugin.process(testCase1)
        plugin.process(testCase2)
        plugin.process(testCase3)
        plugin.process(testCase4)

        assert plugin.timeline.hosts.size() == 2

        def host1 = plugin.timeline.hosts[0]
        assert host1.title == "host1"
        assert host1.threads.size() == 2

        def thread11 = host1.threads[0]
        assert thread11.title == "thread1"
        assert thread11.testCases.size() == 2

        def info1 = thread11.testCases[0]
        use([InvokerHelper, PluginUtils]) {
            assert info1.getProperties() == testCase1.toInfo().getProperties()
        }

        def info2 = thread11.testCases[1]
        use([InvokerHelper, PluginUtils]) {
            assert info2.getProperties() == testCase2.toInfo().getProperties()
        }

        def thread12 = host1.threads[1]
        assert thread12.title == "thread2"
        assert thread12.testCases.size() == 1

        def info3 = thread12.testCases[0]
        use([InvokerHelper, PluginUtils]) {
            assert info3.getProperties() == testCase3.toInfo().getProperties()
        }

        def host2 = plugin.timeline.hosts[1]
        assert host2.title == "host2"
        assert host2.threads.size() == 1

        def thread21 = host2.threads[0]
        assert thread21.title == "thread1"
        assert thread21.testCases.size() == 1

        def info4 = thread21.testCases[0]
        use([InvokerHelper, PluginUtils]) {
            assert info4.getProperties() == testCase4.toInfo().getProperties()
        }
    }

    @Test
    void shouldGetRightType() {
        assert plugin.type == AllureTestCase
    }

    @Test
    void shouldGetRightDataName() {
        assert plugin.pluginData
        assert plugin.pluginData.name == ["timeline.json"]
    }
}
