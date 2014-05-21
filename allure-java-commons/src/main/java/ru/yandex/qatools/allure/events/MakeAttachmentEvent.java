package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.Step;

import static ru.yandex.qatools.allure.utils.AllureResultsUtils.writeAttachmentSafety;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class MakeAttachmentEvent extends AbstractMakeAttachmentEvent {

    public MakeAttachmentEvent(byte[] attachment, String title, String type) {
        setTitle(title);
        setType(type);
        setAttachment(attachment);
    }

    @Override
    public void process(Step step) {
        Attachment attachment = writeAttachmentSafety(getAttachment(), getTitle(), getType());
        step.getAttachments().add(attachment);
    }

}
