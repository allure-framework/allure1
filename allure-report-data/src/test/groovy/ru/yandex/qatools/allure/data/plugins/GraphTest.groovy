package ru.yandex.qatools.allure.data.plugins

import org.codehaus.groovy.runtime.InvokerHelper
import org.junit.Test
import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.utils.PluginUtils

import static ru.yandex.qatools.allure.model.Status.PASSED

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 07.02.15
 */
class GraphTest {

    def plugin = new Graph()

    @Test
    void shouldSaveInfo() {
        def testCase = new AllureTestCase(uid: "uid", name: "name", title: "title", status: PASSED)
        plugin.process(testCase)
        assert plugin.graph.testCases.size() == 1

        use([InvokerHelper, PluginUtils]) {
            assert plugin.graph.testCases[0].getProperties() == testCase.toInfo().getProperties()
        }
    }
}
