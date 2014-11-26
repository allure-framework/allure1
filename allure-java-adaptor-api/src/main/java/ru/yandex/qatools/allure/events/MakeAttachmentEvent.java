package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.Step;

import static ru.yandex.qatools.allure.utils.AllureResultsUtils.writeAttachmentSafely;

/**
 * Using to add attachments to tests.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 *         <p/>
 * @see ru.yandex.qatools.allure.events.StepEvent
 */
public class MakeAttachmentEvent extends AbstractMakeAttachmentEvent {

    /**
     * Constructs an new event with specified binary attachment source, title and MIME-type
     *
     * @param attachment as byte array.
     * @param title      of attachment. Shown at report as name of attachment
     * @param type       MIME-type of attachment
     */
    public MakeAttachmentEvent(byte[] attachment, String title, String type) {
        setTitle(title);
        setType(type);
        setAttachment(attachment);
    }

    /**
     * Write attachment from event using
     * {@link ru.yandex.qatools.allure.utils.AllureResultsUtils#writeAttachmentSafely(byte[], String, String)}
     * Then add attachment to step attachments.
     *
     * @param step to change
     */
    @Override
    public void process(Step step) {
        Attachment attachment = writeAttachmentSafely(getAttachment(), getTitle(), getType());
        step.getAttachments().add(attachment);
    }

}
