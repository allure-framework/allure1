package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.AttachmentType;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class MakeAttachEvent implements Event {
    private String title;
    private AttachmentType attachmentType;
    private Object attach;

    public MakeAttachEvent(String title, AttachmentType attachmentType, Object attach) {
        this.title = title;
        this.attachmentType = attachmentType;
        this.attach = attach;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AttachmentType getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(AttachmentType attachmentType) {
        this.attachmentType = attachmentType;
    }

    public Object getAttach() {
        return attach;
    }

    public void setAttach(Object attach) {
        this.attach = attach;
    }
}
