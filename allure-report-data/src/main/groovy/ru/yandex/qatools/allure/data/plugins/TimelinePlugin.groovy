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
class TimelinePlugin implements ProcessPlugin<AllureTestCase> {

    public static final String TIMELINE_JSON = "timeline.json"

    AllureTimeline timeline = new AllureTimeline();

    private Map<String, Host> hosts = new HashMap<>();

    private Map<Key, Thread> threads = new HashMap<>();

    @Override
    void process(AllureTestCase testCase) {
        use(PluginUtils) {
            def hostName = testCase.getHostValue()
            if (!hosts.containsKey(hostName)) {
                Host host = new Host(title: hostName);
                hosts[hostName] = host;
                timeline.hosts.add(host);
            }

            def threadName = testCase.getThreadValue()
            def key = new Key(host: hostName, thread: threadName)
            if (!threads.containsKey(key)) {
                Thread thread = new Thread(title: threadName);
                threads[key] = thread;
                hosts[hostName].threads.add(thread);
            }

            threads[key].testCases.add(testCase.toInfo());
        }
    }

    @Override
    List<PluginData> getPluginData() {
        return Arrays.asList(new PluginData(TIMELINE_JSON, timeline));
    }

    @Override
    Class<AllureTestCase> getType() {
        return AllureTestCase;
    }

    @EqualsAndHashCode
    class Key {
        String host;
        String thread;
    }
}
