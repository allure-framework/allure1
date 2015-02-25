package ru.yandex.qatools.allure.data.plugins

import ru.yandex.qatools.allure.model.Status
import ru.yandex.qatools.allure.model.TestCaseResult

import static ru.yandex.qatools.allure.config.AllureModelUtils.createSeverityLabel

/**
 * eroshenkoam 
 * 25/02/15
 */
class MigrationPlugin implements PreparePlugin<TestCaseResult> {

    @Override
    void prepare(TestCaseResult data) {
        migrateSeverityFieldToLabel(data)
        migrateSkippedStatusToCanceled(data)
    }

    @Override
    Class<TestCaseResult> getType() {
        TestCaseResult
    }

    static void migrateSkippedStatusToCanceled(TestCaseResult data) {
        if (data.status.equals(Status.SKIPPED)) {
            data.status = Status.CANCELED;
        }
    }

    static void migrateSeverityFieldToLabel(TestCaseResult data) {
        if (data.severity) {
            data.labels.add(createSeverityLabel(data.severity))
            data.severity = null
        }
    }
}
