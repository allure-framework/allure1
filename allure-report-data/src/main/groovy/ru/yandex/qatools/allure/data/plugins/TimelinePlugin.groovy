package ru.yandex.qatools.allure.data.plugins

import groovy.transform.EqualsAndHashCode
import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.AllureTimeline
import ru.yandex.qatools.allure.data.Host
import ru.yandex.qatools.allure.data.Thread
import ru.yandex.qatools.allure.data.utils.PluginUtils

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 07.02.15
 */
@Plugin.Name("timeline")
@Plugin.Priority(200)
class TimelinePlugin extends DefaultTabPlugin {

    @Plugin.Data
    def timeline = new AllureTimeline()

    private Map<String, Host> hosts = [:]

    private Map<Key, Thread> threads = [:]

    @Override
    void process(AllureTestCase testCase) {
        use(PluginUtils) {
            def hostName = testCase.hostValue
            if (!hosts.containsKey(hostName)) {
                def host = new Host(title: hostName)
                hosts[hostName] = host
                timeline.hosts.add(host)
            }

            def threadName = testCase.threadValue
            def key = new Key(host: hostName, thread: threadName)
            if (!threads.containsKey(key)) {
                def thread = new Thread(title: threadName)
                threads[key] = thread
                hosts[hostName].threads.add(thread)
            }

            threads[key].testCases.add(testCase.toInfo())
        }
    }

    @EqualsAndHashCode
    class Key {
        String host
        String thread
    }
}
