package ru.yandex.qatools.allure.data.plugins

import org.codehaus.groovy.runtime.InvokerHelper
import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.AllureTestSuite
import ru.yandex.qatools.allure.data.AllureXUnit
import ru.yandex.qatools.allure.data.ListWidgetData
import ru.yandex.qatools.allure.data.ReportGenerationException
import ru.yandex.qatools.allure.data.Statistic
import ru.yandex.qatools.allure.data.Time
import ru.yandex.qatools.allure.data.XUnitWidgetItem
import ru.yandex.qatools.allure.data.utils.PluginUtils

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.15
 */
@Plugin.Name("xunit")
@Plugin.Priority(500)
class XUnitPlugin extends DefaultTabPlugin implements WithWidget {

    public static final int SUITES_IN_WIDGET = 10

    @Plugin.Data
    def xUnit = new AllureXUnit(time: new Time(start: Long.MAX_VALUE, stop: Long.MIN_VALUE))

    private Map<String, AllureTestSuite> testSuites = [:]

    @Override
    void process(AllureTestCase testCase) {
        if (!testCase.status) {
            throw new ReportGenerationException("Test case status should not be null")
        }

        def uid = testCase.suite.uid
        if (!testSuites.containsKey(uid)) {
            def suite = new AllureTestSuite()

            use(InvokerHelper) {
                suite.properties = testCase.suite.properties
            }

            suite.statistic = new Statistic()
            suite.time = new Time(start: Long.MAX_VALUE, stop: Long.MIN_VALUE)
            testSuites[uid] = suite
            xUnit.testSuites.add(suite)
        }

        def suite = testSuites[uid]

        use(PluginUtils) {
            suite.statistic.update(testCase.status)
            suite.time.update(testCase.time)
            suite.getTestCases().add(testCase.toInfo())

            xUnit.time.update(testCase.time)
        }
    }

    @Override
    Object getWidgetData() {
        def items = xUnit.testSuites
                .sort { a, b -> PluginUtils.cmp(b.statistic, a.statistic) }
                .take(SUITES_IN_WIDGET)
                .collect { new XUnitWidgetItem(uid: it.uid, title: it.title, statistic: it.statistic) }
                .sort { it.title }
        new ListWidgetData(totalCount: xUnit.testSuites.size(), items: items)
    }
}
