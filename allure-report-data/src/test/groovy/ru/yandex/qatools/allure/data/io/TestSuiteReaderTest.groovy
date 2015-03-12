package ru.yandex.qatools.allure.data.io

import org.apache.commons.io.FileUtils
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.yandex.qatools.allure.model.ObjectFactory
import ru.yandex.qatools.allure.model.TestSuiteResult

import javax.xml.bind.JAXB

import static ru.yandex.qatools.allure.config.AllureNamingUtils.generateTestSuiteFileName

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.02.15
 */
class TestSuiteReaderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    void shouldNotHasNextIfNotHasSuiteResult() {
        def reader = getReader([]);
        assert !reader.iterator().hasNext()
        assert reader.iterator().next() == null
    }

    @Test
    void shouldHasNextIfHas() {
        def testSuite = new TestSuiteResult(name: "name")
        def reader = getReader([testSuite]);
        assert reader.iterator().hasNext()
        assert reader.iterator().next() == testSuite
        assert !reader.iterator().hasNext()
    }

    @Test(expected = UnsupportedOperationException)
    void shouldNotRemoveFromIterator() {
        def testSuite = new TestSuiteResult(name: "name")

        def reader = getReader([testSuite]);
        reader.iterator().remove()
    }

    @Test
    void shouldSkipFileIfNotFound() {
        def dir = folder.newFolder();
        def testSuite = new TestSuiteResult(name: "name")

        def file = new File(dir, generateTestSuiteFileName())
        JAXB.marshal(new ObjectFactory().createTestSuite(testSuite), file)

        def reader = new TestSuiteReader(dir);
        FileUtils.deleteQuietly(file)

        assert reader.iterator().hasNext()
        assert reader.iterator().next() == null
        assert !reader.iterator().hasNext()
    }

    def getReader(List<TestSuiteResult> results) {
        def dir = folder.newFolder();
        for (def result : results) {
            JAXB.marshal(new ObjectFactory().createTestSuite(result), new File(dir, generateTestSuiteFileName()))
        }

        new TestSuiteReader(dir);
    }
}
