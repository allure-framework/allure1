package ru.yandex.qatools.allure.data.plugins;

import ru.yandex.qatools.allure.data.AttachmentInfo;

import java.util.List;

/**
 * You can use this index to get information about
 * your report attachments during report generation.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.07.15
 */
public interface AttachmentsIndex {

    /**
     * Finds the attachment by given uid. Returns null if there is
     * no attachment with such uid.
     *
     * @param uid uid to find attachment.
     * @return found attachment info or null if there is no attachment
     * with such uid.
     */
    AttachmentInfo find(String uid);

    /**
     * Finds the attachment by given source. Returns null if there is
     * no attachment with such source.
     *
     * @param source source to find attachment.
     * @return found attachment info or null if there is no attachment
     * with such source.
     */
    AttachmentInfo findBySource(String source);

    /**
     * Returns the list of all attachments found in result directories.
     */
    List<AttachmentInfo> findAll();
}
