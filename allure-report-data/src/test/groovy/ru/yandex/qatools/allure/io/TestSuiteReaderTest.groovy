package ru.yandex.qatools.allure.io

import org.apache.commons.io.FileUtils
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.yandex.qatools.allure.model.ObjectFactory
import ru.yandex.qatools.allure.model.TestSuiteResult
import ru.yandex.qatools.allure.utils.AllureReportUtils

import javax.xml.bind.JAXB

import static groovy.test.GroovyAssert.shouldFail
import static ru.yandex.qatools.allure.AllureUtils.generateTestSuiteJsonName
import static ru.yandex.qatools.allure.AllureUtils.generateTestSuiteXmlName

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.02.15
 */
class TestSuiteReaderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    void shouldNotHasNextIfNotHasSuiteResult() {
        def reader = getReader([])

        def iterator = reader.iterator()
        assert !iterator.hasNext()
        shouldFail(NoSuchElementException, {
            iterator.next()
        })
    }

    @Test
    void shouldHasNextIfHas() {
        def testSuite = new TestSuiteResult(name: "name")
        def reader = getReader([testSuite]);

        def iterator = reader.iterator()
        assert iterator.hasNext()
        assert iterator.next() == testSuite
        assert !iterator.hasNext()
    }

    @Test
    void shouldNotRemoveFromIterator() {
        def testSuite = new TestSuiteResult(name: "name")

        def reader = getReader([testSuite])
        def iterator = reader.iterator()
        iterator.next()
        shouldFail(UnsupportedOperationException, {
            iterator.remove()
        })
    }

    @Test
    void shouldSkipFileIfNotFound() {
        def dir = folder.newFolder()
        def testSuite = new TestSuiteResult(name: "name")

        def file = new File(dir, generateTestSuiteXmlName())
        JAXB.marshal(new ObjectFactory().createTestSuite(testSuite), file)

        def reader = new TestSuiteReader(dir.toPath())
        FileUtils.deleteQuietly(file)


        def iterator = reader.iterator()
        assert !iterator.hasNext()
    }

    @Test
    void shouldReadJsonFilesAsWell() {
        def testSuite = new TestSuiteResult(name: "name")
        def dir = folder.newFolder()
        AllureReportUtils.serialize(dir, generateTestSuiteJsonName(), testSuite)

        def reader = new TestSuiteReader(dir.toPath())


        def iterator = reader.iterator()
        assert iterator.hasNext()
        assert iterator.next() == testSuite
        assert !iterator.hasNext()
    }

    def getReader(List<TestSuiteResult> results) {
        def dir = folder.newFolder();
        for (def result : results) {
            JAXB.marshal(new ObjectFactory().createTestSuite(result), new File(dir, generateTestSuiteXmlName()))
        }

        new TestSuiteReader(dir.toPath())
    }
}
