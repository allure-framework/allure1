package ru.yandex.qatools.allure.data.plugins

import org.codehaus.groovy.runtime.InvokerHelper
import org.junit.Test
import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.AllureTestSuiteInfo
import ru.yandex.qatools.allure.data.ListWidgetData
import ru.yandex.qatools.allure.data.ReportGenerationException
import ru.yandex.qatools.allure.data.Statistic
import ru.yandex.qatools.allure.data.Time
import ru.yandex.qatools.allure.data.utils.PluginUtils

import static ru.yandex.qatools.allure.model.Status.BROKEN
import static ru.yandex.qatools.allure.model.Status.FAILED
import static ru.yandex.qatools.allure.model.Status.PASSED

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 07.02.15
 */
class XUnitPluginTest {

    def plugin = new XUnitPlugin()

    @Test(expected = ReportGenerationException.class)
    void testCaseStatusShouldNotBeNull() {
        def testCase = new AllureTestCase(
                uid: "uid", name: "name"
        )

        plugin.process(testCase)
    }

    @Test
    void shouldGroupBySuite() {
        def testCase = new AllureTestCase(uid: "uid", name: "name", status: PASSED,
                suite: new AllureTestSuiteInfo(uid: "suiteUid", name: "suiteName"),
                time: new Time(start: 1, stop: 10)
        )

        plugin.process(testCase)

        assert plugin.xUnit.time
        assert plugin.xUnit.time.start == 1
        assert plugin.xUnit.time.stop == 10
        assert plugin.xUnit.time.duration == 9

        assert plugin.xUnit.testSuites.size() == 1

        def suite = plugin.xUnit.testSuites[0]
        assert suite.uid == "suiteUid"
        assert suite.name == "suiteName"

        assert suite.time
        assert suite.time.start == 1
        assert suite.time.stop == 10
        assert suite.time.duration == 9

        use(PluginUtils) {
            assert suite.statistic.eq(new Statistic(passed: 1, total: 1))
        }

        assert suite.testCases.size() == 1

        def info = suite.testCases[0]
        use([InvokerHelper, PluginUtils]) {
            assert info.getProperties() == testCase.toInfo().getProperties()
        }

    }

    @Test
    void shouldGroupBySuiteFewTestCases() {
        def testCase1 = new AllureTestCase(uid: "uid1", name: "name1", status: PASSED,
                suite: new AllureTestSuiteInfo(uid: "suiteUid1", name: "suiteName1"),
                time: new Time(start: 3, stop: 10)
        )

        def testCase2 = new AllureTestCase(uid: "uid2", name: "name2", status: FAILED,
                suite: new AllureTestSuiteInfo(uid: "suiteUid1", name: "suiteName1"),
                time: new Time(start: 2, stop: 9)
        )

        def testCase3 = new AllureTestCase(uid: "uid3", name: "name3", status: BROKEN,
                suite: new AllureTestSuiteInfo(uid: "suiteUid2", name: "suiteName2"),
                time: new Time(start: 7, stop: 15)
        )

        plugin.process(testCase1)
        plugin.process(testCase2)
        plugin.process(testCase3)

        assert plugin.xUnit.time
        assert plugin.xUnit.time.start == 2
        assert plugin.xUnit.time.stop == 15
        assert plugin.xUnit.time.duration == 13

        assert plugin.xUnit.testSuites.size() == 2
        assert plugin.xUnit.testSuites.collect { it.name }.containsAll(["suiteName1", "suiteName2"])

        def suite1 = plugin.xUnit.testSuites[0]
        assert suite1.uid == "suiteUid1"
        assert suite1.name == "suiteName1"

        assert suite1.time
        assert suite1.time.start == 2
        assert suite1.time.stop == 10
        assert suite1.time.duration == 8

        use(PluginUtils) {
            assert suite1.statistic.eq(new Statistic(passed: 1, failed: 1, total: 2))
        }

        assert suite1.testCases.size() == 2
        assert suite1.testCases.collect { it.name }.containsAll(["name1", "name2"])

        def suite2 = plugin.xUnit.testSuites[1]
        assert suite2.uid == "suiteUid2"
        assert suite2.name == "suiteName2"

        assert suite2.time
        assert suite2.time.start == 7
        assert suite2.time.stop == 15
        assert suite2.time.duration == 8

        use(PluginUtils) {
            assert suite2.statistic.eq(new Statistic(broken: 1, total: 1))
        }

        assert suite2.testCases.size() == 1
        assert suite2.testCases[0].name == "name3"
    }

    @Test
    void shouldGetRightType() {
        assert plugin.type == AllureTestCase
    }

    @Test
    void shouldGetRightDataName() {
        assert plugin.pluginData
        assert plugin.pluginData.name == ["xunit.json"]
    }

    @Test
    void shouldGenerateEmptyWidget() {
        assert (plugin.widgetData as ListWidgetData).totalCount == 0;
    }

    @Test
    void shouldGenerateWidget() {
        for (int i = 0; i < 20; i++) {
            def testCase = new AllureTestCase(
                    time: new Time(start: 0, stop: i, duration: i),
                    status: FAILED,
                    suite: new AllureTestSuiteInfo(uid: "$i", title: "suite#$i")
            )
            plugin.process(testCase)
        }

        def data = plugin.widgetData as ListWidgetData
        assert data.items.size() == 10

        assert data.items*.title*.startsWith("suite#")
        assert data.items*.statistic*.equals(new Statistic(total: 1, passed: 0, failed: 1,
                broken: 0, canceled: 0, pending: 0))
    }
}
