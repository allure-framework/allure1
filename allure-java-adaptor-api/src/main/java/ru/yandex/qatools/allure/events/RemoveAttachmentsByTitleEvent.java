package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.Step;

import java.util.Iterator;
import java.util.regex.Pattern;

import static ru.yandex.qatools.allure.utils.AllureResultsUtils.deleteAttachment;

/**
 * Using to remove attachments by title from step.
 *
 * @author Maksim Mukosey eoff@yandex-team.ru
 *         Date: 23.05.16
 */
public class RemoveAttachmentsByTitleEvent extends AbstractRemoveAttachmentEvent {

    /**
     * Constructs an new event with pattern compiled from specified regex.
     *
     * @param regex with using to match attachments source
     */
    public RemoveAttachmentsByTitleEvent(String regex) {
        if (regex != null) {
            setPattern(Pattern.compile(regex));
        }
    }

    /**
     * Remove attachments matches pattern from step and all step substeps
     *
     * @param context from which attachments will be removed
     */
    @Override
    public void process(Step context) {
        if (pattern == null) {
            return;
        }
        Iterator<Attachment> iterator = context.getAttachments().listIterator();
        while (iterator.hasNext()) {
            Attachment attachment = iterator.next();
            if (pattern.matcher(attachment.getTitle()).matches()) {
                deleteAttachment(attachment);
                iterator.remove();
            }
        }

        for (Step step : context.getSteps()) {
            process(step);
        }
    }
}
