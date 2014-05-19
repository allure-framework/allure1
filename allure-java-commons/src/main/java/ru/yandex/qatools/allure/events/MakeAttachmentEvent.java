package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.Step;

import static ru.yandex.qatools.allure.utils.AllureResultsUtils.writeAttachmentSafety;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class MakeAttachmentEvent extends AbstractMakeAttachmentEvent {

    public MakeAttachmentEvent(String title, String attachmentType, byte[] attachment) {
        setTitle(title);
        setAttachmentType(attachmentType);
        setAttachment(attachment);
    }

    @Override
    public void process(Step step) {
        Attachment attachment = new Attachment();

        String source = writeAttachmentSafety(getAttachment(), getAttachmentType());

        attachment.setTitle(getTitle());
        attachment.setType(getAttachmentType());
        attachment.setSource(source);

        step.getAttachments().add(attachment);
    }

}
