package ru.yandex.qatools.allure.data.plugins;

import ru.yandex.qatools.allure.data.AttachmentInfo;

import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.07.15
 */
public interface AttachmentIndex {

    AttachmentInfo find(String uid);

    AttachmentInfo findBySource(String source);

    List<AttachmentInfo> findAll();
}
