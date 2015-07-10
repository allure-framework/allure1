package ru.yandex.qatools.allure.data.index

import groovy.transform.CompileStatic
import ru.yandex.qatools.allure.data.AttachmentInfo
import ru.yandex.qatools.allure.data.plugins.AttachmentIndex

import static java.util.Collections.unmodifiableList
import static ru.yandex.qatools.allure.commons.AllureFileUtils.listAttachmentFiles
import static ru.yandex.qatools.allure.data.utils.TextUtils.generateUid

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.07.15
 */
@CompileStatic
class DefaultAttachmentIndex implements AttachmentIndex {

    Map<String, AttachmentInfo> byUid = new HashMap<>()

    Map<String, AttachmentInfo> bySource = new HashMap<>()

    public DefaultAttachmentIndex(File... directories) {
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

    @Override
    AttachmentInfo find(String uid) {
        byUid[uid]
    }

    @Override
    AttachmentInfo findBySource(String source) {
        bySource[source]
    }

    @Override
    List<AttachmentInfo> findAll() {
        unmodifiableList(byUid.values() as List<AttachmentInfo>)
    }
}
