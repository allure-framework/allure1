package ru.yandex.qatools.allure.data.converters

import org.modelmapper.Converter
import org.modelmapper.ModelMapper
import org.modelmapper.spi.MappingContext
import ru.yandex.qatools.allure.data.AllureAttachment
import ru.yandex.qatools.allure.data.AllureStep
import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.AllureTestSuiteInfo
import ru.yandex.qatools.allure.data.Summary
import ru.yandex.qatools.allure.data.utils.PluginUtils
import ru.yandex.qatools.allure.data.utils.SummaryCategory
import ru.yandex.qatools.allure.data.utils.TextUtils
import ru.yandex.qatools.allure.model.Attachment
import ru.yandex.qatools.allure.model.Step
import ru.yandex.qatools.allure.model.TestCaseResult

import static ru.yandex.qatools.allure.data.utils.TextUtils.generateUid

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.15
 */
class DefaultTestCaseConverter implements TestCaseConverter {

    def suiteUids = [:].withDefault {
        generateUid();
    }

    @Override
    AllureTestCase convert(TestCaseResult result) {
        ModelMapper mapper = new ModelMapper();

        mapper.createTypeMap(TestCaseResult.class, AllureTestCase.class).postConverter = new TestCaseResultProcessor();
        mapper.createTypeMap(Step.class, AllureStep.class).postConverter = new StepProcessor();
        mapper.createTypeMap(Attachment.class, AllureAttachment.class).postConverter = new AttachmentProcessor();

        mapper.map(result, AllureTestCase.class);
    }

    class TestCaseResultProcessor implements Converter<TestCaseResult, AllureTestCase> {

        @Override
        public AllureTestCase convert(MappingContext<TestCaseResult, AllureTestCase> context) {
            def result = context.destination;
            def source = context.source;

            use([PluginUtils, SummaryCategory]) {
                result.uid = generateUid();

                if (!result.title) {
                    result.title = TextUtils.humanize(result.name);
                }
                result.description = source.convertedDescription;
                result.time = source.time;

                result.summary = (result.steps.summary.sum() ?: new Summary()) as Summary;
                result.summary.attachments += result.attachments.size();

                result.severity = source.severity;
                result.testId = source.testId
                result.issues = source.issues;
                result.suite = new AllureTestSuiteInfo(
                        uid: suiteUids[source.suiteName],
                        name: source.suiteName,
                        title: source.suiteTitle ?: TextUtils.humanize(source.suiteName)
                );
            }

            result;
        }
    }

    class StepProcessor implements Converter<Step, AllureStep> {
        @Override
        public AllureStep convert(MappingContext<Step, AllureStep> context) {
            def result = context.destination;
            def source = context.source;

            use([PluginUtils, SummaryCategory]) {
                if (!result.title) {
                    result.title = TextUtils.humanize(result.name);
                }

                result.time = source.time;

                result.summary = result.steps.summary.sum() as Summary ?: new Summary();
                result.summary.attachments += result.attachments.size();
            }

            result;
        }
    }

    class AttachmentProcessor implements Converter<Attachment, AllureAttachment> {
        @Override
        public AllureAttachment convert(MappingContext<Attachment, AllureAttachment> context) {
            def result = context.destination;

            result.uid = generateUid();

            result;
        }
    }
}
