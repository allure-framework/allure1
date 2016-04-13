package ru.yandex.qatools.allure.data.plugins

import org.codehaus.groovy.runtime.InvokerHelper
import org.junit.Test
import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.ListWidgetData
import ru.yandex.qatools.allure.data.utils.PluginUtils
import ru.yandex.qatools.allure.model.Failure

import static ru.yandex.qatools.allure.model.Status.BROKEN
import static ru.yandex.qatools.allure.model.Status.CANCELED
import static ru.yandex.qatools.allure.model.Status.FAILED
import static ru.yandex.qatools.allure.model.Status.PASSED
import static ru.yandex.qatools.allure.model.Status.PENDING

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 07.02.15
 */
class DefectsPluginTest {

    def plugin = new DefectsPlugin()

    @Test
    void shouldContainsEmptyDefectItems() {
        assert plugin.defects.defectsList.size() == 2
        assert plugin.defects.defectsList.findAll { it.status == FAILED }.size() == 1
        assert plugin.defects.defectsList.findAll { it.status == BROKEN }.size() == 1
    }

    @Test
    void shouldSkippOtherStatuses() {
        def testCases = [CANCELED, PENDING, PASSED].collect {
            new AllureTestCase(
                    uid: "uid", name: "name", status: it, failure: new Failure(message: "message")
            )
        }

        testCases.each {
            plugin.process(it)
        }

        assert plugin.defects.defectsList.size() == 2
        assert plugin.defects.defectsList.findAll { !it.defects.empty }.size() == 0
    }

    @Test
    void shouldAddFailedItem() {
        def failure = new Failure(message: "message")

        def testCase = new AllureTestCase(
                uid: "uid", name: "name", status: FAILED, failure: failure
        )

        plugin.process(testCase)

        assert plugin.defects.defectsList.size() == 2
        assert plugin.defects.defectsList.findAll { !it.defects.empty }.size() == 1

        def defects = plugin.defects.defectsList.find { it.status == FAILED }.defects
        assert defects.size() == 1
        assert defects[0].failure == failure
        assert defects[0].uid != null
        assert defects[0].testCases.size() == 1

        use([InvokerHelper, PluginUtils]) {
            assert defects[0].testCases[0].getProperties() == testCase.toInfo().getProperties()
        }
    }

    @Test
    void shouldAddBrokenItem() {
        def failure = new Failure(message: "message")
        def testCase = new AllureTestCase(
                uid: "uid", name: "name", status: BROKEN, failure: failure
        )

        plugin.process(testCase)

        assert plugin.defects.defectsList.size() == 2
        assert plugin.defects.defectsList.findAll { !it.defects.empty }.size() == 1

        def defects = plugin.defects.defectsList.find { it.status == BROKEN }.defects
        assert defects.size() == 1
        assert defects[0].failure == failure
        assert defects[0].uid != null
        assert defects[0].testCases.size() == 1

        use([InvokerHelper, PluginUtils]) {
            assert defects[0].testCases[0].getProperties() == testCase.toInfo().getProperties()
        }
    }

    @Test
    void shouldMergeDefectsWithTheSameMessageAndStatus() {
        def failure = new Failure(message: "message")
        def first = new AllureTestCase(
                uid: "uid1", name: "name1", status: BROKEN, failure: failure
        )
        def second = new AllureTestCase(
                uid: "uid1", name: "name1", status: BROKEN, failure: failure
        )

        plugin.process(first)
        plugin.process(second)

        assert plugin.defects.defectsList.size() == 2
        assert plugin.defects.defectsList.findAll { !it.defects.empty }.size() == 1

        def defects = plugin.defects.defectsList.find { it.status == BROKEN }.defects
        assert defects.size() == 1
        assert defects[0].failure == failure
        assert defects[0].uid != null
        assert defects[0].testCases.size() == 2

        use([InvokerHelper, PluginUtils]) {
            assert defects[0].testCases[0].getProperties() == first.toInfo().getProperties()
            assert defects[0].testCases[1].getProperties() == second.toInfo().getProperties()
        }
    }

    @Test
    void shouldNotMergeDefectsWithTheSameMessageAndDifferentStatuses() {
        def failure = new Failure(message: "message")
        def first = new AllureTestCase(
                uid: "uid1", name: "name1", status: BROKEN, failure: failure
        )
        def second = new AllureTestCase(
                uid: "uid1", name: "name1", status: FAILED, failure: failure
        )

        plugin.process(first)
        plugin.process(second)

        assert plugin.defects.defectsList.size() == 2
        assert plugin.defects.defectsList.findAll { !it.defects.empty }.size() == 2

        def broken = plugin.defects.defectsList.find { it.status == BROKEN }.defects
        assert broken.size() == 1
        assert broken[0].failure == failure
        assert broken[0].uid != null
        assert broken[0].testCases.size() == 1

        use([InvokerHelper, PluginUtils]) {
            assert broken[0].testCases[0].getProperties() == first.toInfo().getProperties()
        }

        def failed = plugin.defects.defectsList.find { it.status == FAILED }.defects
        assert failed.size() == 1
        assert failed[0].failure == failure
        assert failed[0].uid != null
        assert failed[0].testCases.size() == 1

        use([InvokerHelper, PluginUtils]) {
            assert failed[0].testCases[0].getProperties() == second.toInfo().getProperties()
        }
    }

    @Test
    void shouldNotMergeDefectsWithDifferentMessagesAndTheSameStatus() {
        def failure1 = new Failure(message: "message1")
        def first = new AllureTestCase(
                uid: "uid1", name: "name1", status: BROKEN, failure: failure1
        )
        def failure2 = new Failure(message: "message2")
        def second = new AllureTestCase(
                uid: "uid1", name: "name1", status: BROKEN, failure: failure2
        )

        plugin.process(first)
        plugin.process(second)

        assert plugin.defects.defectsList.size() == 2
        assert plugin.defects.defectsList.findAll { !it.defects.empty }.size() == 1

        def defects = plugin.defects.defectsList.find { it.status == BROKEN }.defects
        assert defects.size() == 2

        assert defects[0].failure == failure1
        assert defects[0].uid != null
        assert defects[0].testCases.size() == 1

        use([InvokerHelper, PluginUtils]) {
            assert defects[0].testCases[0].getProperties() == first.toInfo().getProperties()
        }

        assert defects[1].failure == failure2
        assert defects[1].uid != null
        assert defects[1].testCases.size() == 1

        use([InvokerHelper, PluginUtils]) {
            assert defects[1].testCases[0].getProperties() == second.toInfo().getProperties()
        }
    }

    @Test
    void shouldGetRightType() {
        assert plugin.type == AllureTestCase
    }

    @Test
    void shouldGetRightDataName() {
        assert plugin.pluginData
        assert plugin.pluginData.name == ["defects.json"]
    }

    @Test
    void shouldGenerateWidget() {
        for (int i = 0; i < 20; i++) {
            def testCase = new AllureTestCase(status: FAILED, failure: new Failure(message: "failure#$i"))
            plugin.process(testCase)
        }

        def data = plugin.widgetData as ListWidgetData
        assert data.totalCount == 20
        assert data.items.size() == 10

        assert data.items*.message*.startsWith("failure#")
    }

    @Test
    void shouldGenerateWidgetWithBothStatuses() {
        for (int i = 0; i < 6; i++) {
            def testCase = new AllureTestCase(status: BROKEN, failure: new Failure(message: "broken#$i"))
            plugin.process(testCase)
        }
        for (int i = 0; i < 6; i++) {
            def testCase = new AllureTestCase(status: FAILED, failure: new Failure(message: "failed#$i"))
            plugin.process(testCase)
        }

        def data = plugin.widgetData as ListWidgetData
        assert data.items.size() == 10

        assert data.items*.message.take(6)*.startsWith("failure#")
        assert data.items*.message.takeRight(4)*.startsWith("broken#")
    }
}
