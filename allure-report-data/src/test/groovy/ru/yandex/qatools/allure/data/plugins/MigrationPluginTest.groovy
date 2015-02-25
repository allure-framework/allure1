package ru.yandex.qatools.allure.data.plugins

import org.junit.Test
import ru.yandex.qatools.allure.config.AllureModelUtils
import ru.yandex.qatools.allure.model.SeverityLevel
import ru.yandex.qatools.allure.model.Status
import ru.yandex.qatools.allure.model.TestCaseResult

/**
 * eroshenkoam 
 * 25/02/15
 */
class MigrationPluginTest {

    def plugin = new MigrationPlugin()

    def severity = SeverityLevel.CRITICAL

    @Test
    void severityFieldMustBeMigratedToLabels() {

        def testCase = new TestCaseResult(severity: severity)
        plugin.prepare(testCase)

        assert testCase.labels.contains(AllureModelUtils.createSeverityLabel(severity))
        assert testCase.severity == null

    }
    
    @Test
    void skippedStatusMustBeMigratedToCanceled () {

        def testCase = new TestCaseResult(status: Status.SKIPPED)
        plugin.prepare(testCase)

        assert testCase.status == Status.CANCELED

    }
}
