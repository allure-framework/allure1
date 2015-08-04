package ru.yandex.qatools.allure.data.utils

import org.junit.Test
import ru.yandex.qatools.allure.model.Description
import ru.yandex.qatools.allure.model.DescriptionType
import ru.yandex.qatools.allure.model.TestCaseResult
import ru.yandex.qatools.allure.model.TestSuiteResult

import static java.lang.System.lineSeparator
import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.equalTo
import static org.hamcrest.Matchers.nullValue
import static ru.yandex.qatools.allure.data.utils.DescriptionUtils.mergeDescriptions

/**
 * eroshenkoam 
 * 11/03/15
 */
class DescriptionUtilsTest {


    @Test
    void descriptionsValuesShouldBeMergedWhenText() {

        def testCaseDescription = new Description(value: "TestCase Description", type: DescriptionType.TEXT)
        def testSuiteDescription = new Description(value: "TestSuite Description", type: DescriptionType.TEXT)

        String expectedDescriptionValue = testSuiteDescription.value + lineSeparator() + testCaseDescription.value;

        def mergedDescription = mergeDescriptions(testSuiteDescription, testCaseDescription)
        assertThat(mergedDescription.value, equalTo(expectedDescriptionValue))
    }

    @Test
    void descriptionsValuesShouldBeMergedWhenMarkdown() {

        def testCaseDescription = new Description(value: "TestCase Description", type: DescriptionType.MARKDOWN)
        def testSuiteDescription = new Description(value: "TestSuite Description", type: DescriptionType.MARKDOWN)

        String expectedDescriptionValue = testSuiteDescription.value + lineSeparator() + lineSeparator() + testCaseDescription.value;

        def mergedDescription = mergeDescriptions(testSuiteDescription, testCaseDescription)
        assertThat(mergedDescription.value, equalTo(expectedDescriptionValue))
    }

    @Test
    void sameTextDescriptionsShouldBeMergedAsText() {

        def testCaseDescription = new Description(value: "TestCase Description", type: DescriptionType.TEXT)
        def testSuiteDescription = new Description(value: "TestSuite Description", type: DescriptionType.TEXT)

        def mergedDescription = mergeDescriptions(testSuiteDescription, testCaseDescription)

        assertThat(mergedDescription.type, equalTo(DescriptionType.TEXT))

    }

    @Test
    void sameMarkdownDescriptionsShouldBeMergedAsMarkdown() {

        def testCaseDescription = new Description(value: "TestCase Description", type: DescriptionType.MARKDOWN)
        def testSuiteDescription = new Description(value: "TestSuite Description", type: DescriptionType.MARKDOWN)

        def mergedDescription = mergeDescriptions(testSuiteDescription, testCaseDescription)

        assertThat(mergedDescription.type, equalTo(DescriptionType.MARKDOWN))

    }


    @Test
    void testSuiteMarkdownDescriptionShouldOverrideTestCaseDescriptionTypeAsMarkdown() {

        def testCaseDescription = new Description(value: "TestCase Description", type: DescriptionType.TEXT)
        def testSuiteDescription = new Description(value: "TestSuite Description", type: DescriptionType.MARKDOWN)

        def mergedDescription = mergeDescriptions(testSuiteDescription, testCaseDescription)

        assertThat(mergedDescription.type, equalTo(DescriptionType.MARKDOWN))

    }

    @Test
    void testCaseMarkdownDescriptionShouldNotBeOverriddenByTestSuiteTextDescription() {

        def testCaseDescription = new Description(value: "TestCase Description", type: DescriptionType.MARKDOWN)
        def testSuiteDescription = new Description(value: "TestSuite Description", type: DescriptionType.TEXT)

        def mergedDescription = mergeDescriptions(testSuiteDescription, testCaseDescription)

        assertThat(mergedDescription.type, equalTo(DescriptionType.MARKDOWN))

    }

    @Test
    void nullTestSuiteDescriptionShouldNotBeMergedWithTestCaseDescription() {

        def testCaseDescription = new Description(value: "TestCase Description", type: DescriptionType.MARKDOWN)
        def mergedDescription = mergeDescriptions(null, testCaseDescription)

        assertThat(mergedDescription.type, equalTo(testCaseDescription.type))
        assertThat(mergedDescription.value, equalTo(testCaseDescription.value))

    }

    @Test
    void nullTestCaseDescriptionShouldBeMergedWithTestSuiteDescription() {

        def testSuiteDescription = new Description(value: "TestSuite Description", type: DescriptionType.MARKDOWN)
        def mergedDescription = mergeDescriptions(testSuiteDescription, null)

        assertThat(mergedDescription.type, equalTo(testSuiteDescription.type))
        assertThat(mergedDescription.value, equalTo(testSuiteDescription.value))

    }

    @Test
    void bothNullTestCaseAndTestSuiteDescriptionShouldMergedIntoNullDescription() {

        def mergedDescription = mergeDescriptions((Description) null, (Description) null)
        assertThat(mergedDescription, nullValue())

    }

    @Test
    void testCaseAndTestSuiteMethodSignatureTest() {

        def testCaseDescription = new Description(value: "TestCase Description", type: DescriptionType.MARKDOWN)
        def testSuiteDescription = new Description(value: "TestSuite Description", type: DescriptionType.TEXT)

        def testCase = new TestCaseResult(description: testCaseDescription)
        def testSuite = new TestSuiteResult(description: testSuiteDescription)

        def actualDescription = mergeDescriptions(testSuite, testCase)
        def expectedDescription = mergeDescriptions(testSuiteDescription, testCaseDescription)

        assertThat(actualDescription.type, equalTo(expectedDescription.type))
        assertThat(actualDescription.value, equalTo(expectedDescription.value))

    }

}
