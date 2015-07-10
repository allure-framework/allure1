package ru.yandex.qatools.allure.data.index

import com.google.inject.Inject
import com.google.inject.Singleton
import groovy.transform.CompileStatic
import ru.yandex.qatools.allure.data.AttachmentInfo
import ru.yandex.qatools.allure.data.io.ResultDirectories
import ru.yandex.qatools.allure.data.plugins.AttachmentsIndex

import static java.util.Collections.unmodifiableList
import static ru.yandex.qatools.allure.commons.AllureFileUtils.listAttachmentFiles
import static ru.yandex.qatools.allure.data.utils.TextUtils.generateUid

/**
 * The default implementation of {@link AttachmentsIndex}.
 * There is two different indexes: by id and by source.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.07.15
 */
@Singleton
@CompileStatic
class DefaultAttachmentsIndex implements AttachmentsIndex {

    Map<String, AttachmentInfo> byUid = new HashMap<>()

    Map<String, AttachmentInfo> bySource = new HashMap<>()

    /**
     * Creates an instance of index.
     * @param directories the directories to find attachments.
     * Should not be empty.
     */
    @Inject
    public DefaultAttachmentsIndex(@ResultDirectories File... directories) {
        for (def file : listAttachmentFiles(directories)) {
            def uid = generateUid()
            def source = file.name
            def attachment = new AttachmentInfo(
                    uid: uid,
                    source: source,
                    size: file.length(),
                    path: file.absolutePath
            )
            byUid[uid] = attachment
            bySource[source] = attachment
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    AttachmentInfo find(String uid) {
        byUid[uid]
    }

    /**
     * @inheritDoc
     */
    @Override
    AttachmentInfo findBySource(String source) {
        bySource[source]
    }

    /**
     * @inheritDoc
     */
    @Override
    List<AttachmentInfo> findAll() {
        unmodifiableList(byUid.values() as List<AttachmentInfo>)
    }
}
