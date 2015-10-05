package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Step;

import java.util.regex.Pattern;

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
     * Do nothing.
     *
     * @param context from which attachments will be removed
     */
    @Override
    public void process(Step context) {
        //do nothing.
    }
}
