package ru.yandex.qatools.allure.plugins

import groovy.transform.CompileStatic
import ru.yandex.qatools.allure.AttachmentInfo

import static java.util.Collections.unmodifiableList

/**
 * The default implementation of {@link AttachmentsIndex}.
 * There is two different indexes: by id and by source.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.07.15
 */
@CompileStatic
class DefaultAttachmentsIndex implements AttachmentsIndex {

    private final Map<String, AttachmentInfo> byUid = [:]

    private final Map<String, AttachmentInfo> bySource = [:]

    /**
     * Creates an instance of index.
     */
    public DefaultAttachmentsIndex(List<AttachmentInfo> infoList) {
        for (def info : infoList) {
            byUid[info.uid] = info
            bySource[info.source] = info
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
