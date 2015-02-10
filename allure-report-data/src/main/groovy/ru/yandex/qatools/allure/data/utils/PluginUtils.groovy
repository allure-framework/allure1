package ru.yandex.qatools.allure.data.utils

import org.codehaus.groovy.runtime.InvokerHelper
import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.AllureTestCaseInfo
import ru.yandex.qatools.allure.data.Issue
import ru.yandex.qatools.allure.data.Statistic
import ru.yandex.qatools.allure.data.TestId
import ru.yandex.qatools.allure.data.Time
import ru.yandex.qatools.allure.data.io.TestCaseReader
import ru.yandex.qatools.allure.model.Description
import ru.yandex.qatools.allure.model.LabelName
import ru.yandex.qatools.allure.model.SeverityLevel
import ru.yandex.qatools.allure.model.Status
import ru.yandex.qatools.allure.model.Step
import ru.yandex.qatools.allure.model.TestCaseResult

import static ru.yandex.qatools.allure.model.DescriptionType.MARKDOWN
import static ru.yandex.qatools.allure.model.LabelName.FEATURE
import static ru.yandex.qatools.allure.model.LabelName.ISSUE
import static ru.yandex.qatools.allure.model.LabelName.SEVERITY
import static ru.yandex.qatools.allure.model.LabelName.STORY

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.15
 */
final class PluginUtils {

    public static final String DEFAULT_FEATURE = "Without feature"
    public static final String DEFAULT_STORY = "Without story"
    public static final String DEFAULT_HOST = "default"
    public static final String DEFAULT_THREAD = "default"

    private PluginUtils() {
    }

//    Step

    static def getTime(Step step) {
        new Time(start: step.start, stop: step.stop, duration: step.stop - step.start);
    }

//    TestCaseResult

    static def getSuiteName(TestCaseResult testCase) {
        getLabelValue(testCase, TestCaseReader.SUITE_NAME);
    }

    static def getSuiteTitle(TestCaseResult testCase) {
        getLabelValue(testCase, TestCaseReader.SUITE_TITLE);
    }

    static def getConvertedDescription(TestCaseResult testCase) {
        switch (testCase?.description?.type) {
            case MARKDOWN:
                return new Description(value: TextUtils.processMarkdown(testCase.description.value), type: MARKDOWN);
        }
        return null;
    }

    static def getTime(TestCaseResult testCase) {
        new Time(start: testCase.start, stop: testCase.stop, duration: testCase.stop - testCase.start);
    }

    static def getTestId(TestCaseResult testCase) {
        def name = getLabelValue(testCase, LabelName.TEST_ID);
        name ? new TestId(name: name, url: TextUtils.getTestUrl(name)) : null;
    }

    static def getIssues(TestCaseResult testCase) {
        def values = getLabelValues(testCase, ISSUE);
        values?.collect { new Issue(name: it, url: TextUtils.getIssueUrl(it)) }
    }

    static def getSeverity(TestCaseResult testCase) {
        def value = getLabelValue(testCase, SEVERITY);
        value ? SeverityLevel.fromValue(value) : SeverityLevel.NORMAL;
    }

    static def getLabelValue(TestCaseResult testCase, LabelName name) {
        getLabelValue(testCase, name.value());
    }

    static def getLabelValue(TestCaseResult testCase, String name) {
        testCase.labels.find { it.name == name }?.value;
    }

    static def getLabelValues(TestCaseResult testCase, LabelName name) {
        testCase.labels.findAll { it.name == name.value() }?.value;
    }

//    AllureTestCase

    static def toInfo(AllureTestCase testCase) {
        use(InvokerHelper) {
            def info = new AllureTestCaseInfo();
            info.properties = testCase.properties;
            info;
        }
    }

    static def getThreadValue(AllureTestCase testCase) {
        getLabelValue(testCase, LabelName.THREAD) ?: DEFAULT_THREAD;
    }

    static def getHostValue(AllureTestCase testCase) {
        getLabelValue(testCase, LabelName.HOST) ?: DEFAULT_HOST;
    }

    static def getStoryValues(AllureTestCase testCase) {
        getLabelValues(testCase, STORY) ?: [DEFAULT_STORY];
    }

    static def getFeatureValues(AllureTestCase testCase) {
        getLabelValues(testCase, FEATURE) ?: [DEFAULT_FEATURE];
    }

    static def getLabelValue(AllureTestCase testCase, LabelName name) {
        testCase.labels.find { it.name == name.value() }?.value;
    }

    static def getLabelValues(AllureTestCase testCase, LabelName name) {
        testCase.labels.findAll { it.name == name.value() }?.value;
    }

//    Time

    static def update(Time first, Time second) {
        first.start = [first.start, second.start].min();
        first.stop = [first.stop, second.stop].max();
        first.duration = first.stop - first.start;
    }

//    Statistic

    static def update(Statistic stats, Status status) {
        def value = status.value();
        stats."$value"++;
        stats.total++;
    }

//    Other

    static def copy(Object from, Object to) {
        use(InvokerHelper) {
            to.properties = from.properties;
            to;
        }
    }

    static <T> T clone(T from) {
        copy(from, from.class.newInstance());
    }
}
