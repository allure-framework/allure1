package ru.yandex.qatools.allure.data.io

import org.junit.Ignore
import org.junit.Test
import ru.yandex.qatools.allure.data.utils.PluginUtils
import ru.yandex.qatools.allure.model.Description
import ru.yandex.qatools.allure.model.DescriptionType
import ru.yandex.qatools.allure.model.Label
import ru.yandex.qatools.allure.model.TestCaseResult
import ru.yandex.qatools.allure.model.TestSuiteResult

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.equalTo
import static ru.yandex.qatools.allure.data.utils.DescriptionUtils.mergeDescriptions

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

    @Test
    void shouldRemoveDuplicateLabels() {
        def first = new Label(name: "someName", value: "someValue")
        def second = new Label(name: "someName", value: "someValue")
        def testCase = new TestCaseResult(name: "testCase", labels: [first, second])

        def testSuite = new TestSuiteResult(name: "name", title: "title", testCases: [testCase], labels: [first, second])

        def reader = getReader([testSuite]);

        def iterator = reader.iterator()
        def next = iterator.next()
        use(PluginUtils) {
            assert next.labels.size() == 3
            assert next.labels.contains(first)
            assert next.labels.findAll { it.equals(first) }.size() == 1
        }
    }

    @Test(expected = UnsupportedOperationException)
    void shouldNotRemoveFromIterator() {
        def testCase = new TestCaseResult(name: "testCase")
        def testSuite = new TestSuiteResult(name: "name", testCases: [testCase])

        def reader = getReader([testSuite]);
        reader.iterator().remove()
    }

    @Test
    void shouldMergeTestSuiteIntoTestCaseDescription() {
        def testCaseDescription = new Description(value: "Test Case Description", type: DescriptionType.TEXT)
        def testCase = new TestCaseResult(description: testCaseDescription)

        def testSuiteDescription = new Description(value: "Test Suite Description", type: DescriptionType.TEXT)
        def testSuite = new TestSuiteResult(testCases: [testCase], description: testSuiteDescription)

        def reader = getReader([testSuite])
        def testCaseUnderTest = reader.iterator().next()

        assert testCaseUnderTest.description != null

        def expectedDescription = mergeDescriptions(testSuiteDescription, testCaseDescription);

        assertThat(testCaseUnderTest.description.type, equalTo(expectedDescription.type))
        assertThat(testCaseUnderTest.description.value, equalTo(expectedDescription.value))
    }

    @Test
    void shouldEscapeInvalidXmlCharacters() {
        def suiteReader = new TestSuiteReader(new File(getClass().classLoader.getResource("testresults").getFile()))
        def reader = new TestCaseReader(suiteReader)

        def iterator = reader.iterator()
        assert iterator.hasNext()
        def next = iterator.next()

        assert next
        assert next.name == '&1234567890someStep '
        assert next.title == 'Ω≈ç√∫˜≤≥ç!@#\$%^*()йцукенгшщзхъфывапролджэёячсмитьбю  '
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
