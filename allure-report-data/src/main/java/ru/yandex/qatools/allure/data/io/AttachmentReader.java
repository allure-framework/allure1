package ru.yandex.qatools.allure.data.io;

import com.google.inject.Inject;
import ru.yandex.qatools.allure.config.AllureConfig;
import ru.yandex.qatools.allure.data.AttachmentInfo;

import java.io.File;
import java.util.Iterator;

import static ru.yandex.qatools.allure.commons.AllureFileUtils.listFilesByRegex;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 19.02.15
 */
public class AttachmentReader implements Reader<AttachmentInfo> {

    private Iterator<File> iterator;

    @Inject
    public AttachmentReader(@ResultDirectories File... inputDirectories) {
        AllureConfig config = AllureConfig.newInstance();
        iterator = listFilesByRegex(
                config.getAttachmentFileRegex(),
                inputDirectories
        ).iterator();
    }

    @Override
    public Iterator<AttachmentInfo> iterator() {
        return new AttachmentInfoIterator();
    }

    private class AttachmentInfoIterator implements Iterator<AttachmentInfo> {
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public AttachmentInfo next() {
            if (!hasNext()) {
                return null;
            }

            File next = iterator.next();
            AttachmentInfo info = new AttachmentInfo();
            info.setName(next.getName());
            info.setPath(next.getAbsolutePath());
            return info;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
