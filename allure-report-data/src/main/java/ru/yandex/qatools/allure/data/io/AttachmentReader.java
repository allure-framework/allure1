package ru.yandex.qatools.allure.data.io;

import com.google.inject.Inject;
import ru.yandex.qatools.allure.data.AttachmentInfo;
import ru.yandex.qatools.allure.data.plugins.AttachmentIndex;

import java.util.Iterator;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 19.02.15
 */
public class AttachmentReader implements Reader<AttachmentInfo> {

    @Inject
    private AttachmentIndex index;

    @Override
    public Iterator<AttachmentInfo> iterator() {
        return index.findAll().iterator();
    }

    public AttachmentIndex getIndex() {
        return index;
    }

    public void setIndex(AttachmentIndex index) {
        this.index = index;
    }
}
