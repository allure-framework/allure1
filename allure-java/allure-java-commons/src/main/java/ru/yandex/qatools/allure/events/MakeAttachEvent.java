package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.AttachmentType;
import ru.yandex.qatools.allure.model.Step;

import static ru.yandex.qatools.allure.utils.AllureWriteUtils.writeAttachment;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class MakeAttachEvent extends AbstractMakeAttachEvent {

    public MakeAttachEvent(String title, AttachmentType attachmentType, Object attach) {
        setTitle(title);
        setAttachmentType(attachmentType);
        setAttach(attach);
    }

    @Override
    public void process(Step step) {
        Attachment attachment = new Attachment();

        String source = writeAttachment(
                getAttach(),
                getAttachmentType(),
                ".attach"
        );

        attachment.setTitle(getTitle());
        attachment.setType(getAttachmentType());
        attachment.setSource(source);

        step.getAttachments().add(attachment);
    }

}
