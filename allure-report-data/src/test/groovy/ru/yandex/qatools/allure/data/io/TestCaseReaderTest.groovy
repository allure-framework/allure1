package ru.yandex.qatools.allure.data.io

import org.junit.Ignore
import org.junit.Test
import ru.yandex.qatools.allure.data.utils.PluginUtils
import ru.yandex.qatools.allure.model.Label
import ru.yandex.qatools.allure.model.TestCaseResult
import ru.yandex.qatools.allure.model.TestSuiteResult

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.02.15
 */
class TestCaseReaderTest {

    @Test
    void shouldNotHasNextIfNotHasSuiteResult() {
        def reader = getReader([]);
        assert !reader.iterator().hasNext()
        assert reader.iterator().next() == null
    }

    @Test
    void shouldNotHasNextIfNotHasTestCaseResult() {
        def testSuite = new TestSuiteResult(name: "name")
        def reader = getReader([testSuite]);
        assert !reader.iterator().hasNext()
        assert reader.iterator().next() == null
    }

    @Test
    void shouldHasNextIfHas() {
        def testCase = new TestCaseResult(name: "testCase")
        def testSuite = new TestSuiteResult(name: "name", testCases: [testCase])
        def reader = getReader([testSuite]);
        assert reader.iterator().hasNext()
        assert reader.iterator().next() == testCase
        assert !reader.iterator().hasNext()
    }

    @Test
    void shouldIterateThroughSuiteResults() {
        def testCase1 = new TestCaseResult(name: "testCase1")
        def testCase2 = new TestCaseResult(name: "testCase2")
        def testSuite1 = new TestSuiteResult(name: "name1", testCases: [testCase1])
        def testSuite2 = new TestSuiteResult(name: "name2", testCases: [testCase2])
        def reader = getReader([testSuite1, testSuite2]);
        assert reader.iterator().hasNext()
        assert reader.iterator().next() == testCase1
        assert reader.iterator().hasNext()
        assert reader.iterator().next() == testCase2
        assert !reader.iterator().hasNext()
    }

    @Ignore("Feature don't implemented. Seems like it's redundant logic")
    @Test
    void shouldSkipNullSuiteResults() {
        def testCase1 = new TestCaseResult(name: "testCase1")
        def testCase2 = new TestCaseResult(name: "testCase2")
        def testSuite1 = new TestSuiteResult(name: "name1", testCases: [testCase1])
        def testSuite2 = new TestSuiteResult(name: "name2", testCases: [testCase2])
        def reader = getReader([testSuite1, null, testSuite2]);
        assert reader.iterator().hasNext()
        assert reader.iterator().next() == testCase1
        assert reader.iterator().hasNext()
        assert reader.iterator().next() == testCase2
        assert !reader.iterator().hasNext()
    }

    @Test
    void shouldSkipEmptySuiteResults() {
        def testCase = new TestCaseResult(name: "testCase")
        def testSuite1 = new TestSuiteResult(name: "name1", testCases: [])
        def testSuite2 = new TestSuiteResult(name: "name2", testCases: [testCase])
        def reader = getReader([testSuite1, testSuite2]);
        assert reader.iterator().hasNext()
        assert reader.iterator().next() == testCase
        assert !reader.iterator().hasNext()
    }

    @Test
    void shouldAddSuiteInformationToCases() {
        def testCase = new TestCaseResult(name: "testCase")

        def label = new Label(name: "someName", value: "someValue")
        def testSuite = new TestSuiteResult(name: "name", title: "title", testCases: [testCase], labels: [label])

        def reader = getReader([testSuite]);

        def next = reader.iterator().next()
        use(PluginUtils) {
            assert next
            assert next.getSuiteName() == "name"
            assert next.getSuiteTitle() == "title"
            assert next.getLabels().contains(label)
        }
    }

    @Test(expected = UnsupportedOperationException)
    void shouldNotRemoveFromIterator() {
        def testCase = new TestCaseResult(name: "testCase")
        def testSuite = new TestSuiteResult(name: "name", testCases: [testCase])

        def reader = getReader([testSuite]);
        reader.iterator().remove()
    }

    static def getReader(List<TestSuiteResult> testSuites) {
        new TestCaseReader(new Reader<TestSuiteResult>() {
            @Override
            Iterator<TestSuiteResult> iterator() {
                testSuites.iterator();
            }
        })
    }
}
