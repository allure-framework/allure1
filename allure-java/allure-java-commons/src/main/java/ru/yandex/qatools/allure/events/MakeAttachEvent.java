package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.AttachmentType;
import ru.yandex.qatools.allure.model.Step;

import static ru.yandex.qatools.allure.utils.AllureWriteUtils.writeAttachment;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class MakeAttachEvent implements StepEvent {
    private String title;
    private AttachmentType attachmentType;
    private Object attach;

    public MakeAttachEvent() {
    }

    @Override
    public void process(Step step) {
        Attachment attachment = new Attachment();

        String source = writeAttachment(
                attach,
                attachmentType,
                ".attach"
        );

        attachment.setTitle(title);
        attachment.setType(attachmentType);
        attachment.setSource(source);

        step.getAttachments().add(attachment);
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

    public MakeAttachEvent withTitle(String title) {
        setTitle(title);
        return this;
    }

    public MakeAttachEvent withType(AttachmentType type) {
        setAttachmentType(type);
        return this;
    }

    public MakeAttachEvent withAttach(Object attach) {
        setAttach(attach);
        return this;
    }
}
