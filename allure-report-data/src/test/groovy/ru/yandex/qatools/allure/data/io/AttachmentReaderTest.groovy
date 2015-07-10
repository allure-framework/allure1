package ru.yandex.qatools.allure.data.io

import org.apache.commons.io.FileUtils
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import ru.yandex.qatools.allure.data.index.DefaultAttachmentsIndex

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.02.15
 */
class AttachmentReaderTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    void shouldNotHasNextIfNoFiles() {
        def reader = getReader([]);
        assert !reader.iterator().hasNext()
    }

    @Test
    void shouldHasNextIfHasAttachments() {
        def reader = getReader(["some"]);
        assert reader.iterator().hasNext()

        def next = reader.iterator().next()
        assert next
        assert next.source == "some-attachment.txt"
        assert next.path
    }

    def getReader(List<String> attachmentNames) {
        def dir = folder.newFolder();

        attachmentNames.each {
            FileUtils.writeStringToFile(createFile(dir, it), "test-content")
        }

        new AttachmentReader(index: new DefaultAttachmentsIndex(dir))
    }

    static def createFile(File dir, String name) {
        new File(dir, name + "-attachment.txt")
    }
}
