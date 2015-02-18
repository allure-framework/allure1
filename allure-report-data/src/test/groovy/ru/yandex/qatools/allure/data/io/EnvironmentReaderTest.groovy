package ru.yandex.qatools.allure.data.io

import org.apache.commons.io.FileUtils
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.yandex.qatools.commons.model.Environment
import ru.yandex.qatools.commons.model.ObjectFactory

import javax.xml.bind.JAXB

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.02.15
 */
class EnvironmentReaderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    void shouldNotHasNextIfDirectoriesIsEmpty() {
        def reader = getReader([]);
        assert !reader.iterator().hasNext()
        assert reader.iterator().next() == null
    }

    @Test
    void shouldFindXml() {
        def env = new Environment(id: "id", name: "name")
        def reader = getReader([env]);
        assert reader.iterator().hasNext()
        assert reader.iterator().next() == env
        assert !reader.iterator().hasNext()
    }

    @Test
    void shouldFindProperties() {
        def props = new Properties()
        props.setProperty("a", "b")

        def reader = getReader([props]);
        assert reader.iterator().hasNext()

        Environment next = reader.iterator().next()
        assert next
        assert !next.id
        assert !next.name
        assert next.parameter
        assert next.parameter.size() == 1
        assert next.parameter.collect { it.name }.contains("a")
        assert next.parameter.collect { it.key }.contains("a")
        assert next.parameter.collect { it.value }.contains("b")
        assert !reader.iterator().hasNext()
    }

    @Test(expected = UnsupportedOperationException)
    void shouldNotRemoveFromIterator() {
        def env = new Environment(id: "id", name: "name")

        def reader = getReader([env]);
        reader.iterator().remove()
    }

    @Test
    void shouldSkipBadFilesInTheEnd() {
        def dir = folder.newFolder();

        FileUtils.writeStringToFile(createFile(dir, "xml"), "first")
        FileUtils.writeStringToFile(createFile(dir, "xml"), "second")

        def reader = new EnvironmentReader(dir);
        assert reader.iterator().hasNext()

        def next = reader.iterator().next()
        assert next == null
    }

    @Test
    void shouldSkipBadFiles() {
        def dir = folder.newFolder();

        def file = createFile(dir, "properties")
        FileUtils.writeStringToFile(file, ":")

        def env = new Environment(id: "id", name: "name")
        JAXB.marshal(new ObjectFactory().createEnvironment(env), createFile(dir, "xml"))

        def reader = new EnvironmentReader(dir);
        assert reader.iterator().hasNext()

        FileUtils.deleteQuietly(file);

        def next = reader.iterator().next()
        assert next
        assert next.id == "id"
        assert next.name == "name"
        assert next.parameter.empty
    }

    def getReader(List<Object> environment) {
        def dir = folder.newFolder();
        for (def env : environment) {
            if (env instanceof Environment) {
                JAXB.marshal(new ObjectFactory().createEnvironment(env as Environment), createFile(dir, "xml"))
            } else if (env instanceof Properties) {
                (env as Properties).store(new FileOutputStream(createFile(dir, "properties")), "some description")
            }
        }

        new EnvironmentReader(dir);
    }

    static def createFile(File dir, String ext) {
        new File(dir, UUID.randomUUID().toString() + "-environment." + ext)
    }
}
