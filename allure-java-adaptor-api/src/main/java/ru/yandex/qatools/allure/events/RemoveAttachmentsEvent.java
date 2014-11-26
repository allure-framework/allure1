package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.Step;

import java.util.Iterator;
import java.util.regex.Pattern;

import static ru.yandex.qatools.allure.utils.AllureResultsUtils.deleteAttachment;

/**
 * Using to remove attachments from step.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 30.04.14
 */
public class RemoveAttachmentsEvent extends AbstractRemoveAttachmentEvent {

    /**
     * Constructs an new event with pattern compiled from specified regex.
     *
     * @param regex with using to match attachments source
     */
    public RemoveAttachmentsEvent(String regex) {
        setPattern(Pattern.compile(regex));
    }

    /**
     * Remove attachments matches pattern from step and all step substeps
     *
     * @param context from which attachments will be removed
     */
    @Override
    public void process(Step context) {
        Iterator<Attachment> iterator = context.getAttachments().listIterator();
        while (iterator.hasNext()) {
            Attachment attachment = iterator.next();
            if (pattern.matcher(attachment.getSource()).matches()) {
                deleteAttachment(attachment);
                iterator.remove();
            }
        }

        for (Step step : context.getSteps()) {
            process(step);
        }
    }
}
