package ru.yandex.qatools.allure.data.converters

import org.junit.Test
import ru.yandex.qatools.allure.model.Label
import ru.yandex.qatools.allure.model.LabelName
import ru.yandex.qatools.allure.model.TestCaseResult

import static ru.yandex.qatools.allure.config.AllureModelUtils.createSeverityLabel
import static ru.yandex.qatools.allure.model.SeverityLevel.CRITICAL
import static ru.yandex.qatools.allure.model.SeverityLevel.NORMAL
import static ru.yandex.qatools.allure.model.Status.PASSED

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.02.15
 */
class DefaultTestCaseConverterTest {

    def converter = new DefaultTestCaseConverter();

    @Test
    void shouldConvertEmptyTestCase() {
        def origin = new TestCaseResult(name: "name", status: PASSED)
        def modify = converter.convert(origin)
        assert modify
        assert modify.name == "name"
        assert modify.title == "Name"

        assert !modify.description
        assert !modify.failure

        assert modify.status == PASSED
        assert modify.severity == NORMAL

        assert modify.attachments.empty
        assert modify.issues.empty
        assert modify.labels.empty
        assert modify.parameters.empty
        assert modify.steps.empty

        assert modify.suite
    }

    @Test
    void shouldGetSeverityFromLabels() {
        def origin = new TestCaseResult(name: "name", labels: [createSeverityLabel(CRITICAL)])
        def modify = converter.convert(origin)

        use(DefaultTestCaseConverterTest) {
            modify.labels.checkLabel(LabelName.SEVERITY, CRITICAL.value())
        }
    }


    static def checkLabel(List<Label> labels, LabelName name, String expectedValue) {
        def found = labels.find { it.name == name.value() }
        assert found
        assert found.value == expectedValue
    }

}
