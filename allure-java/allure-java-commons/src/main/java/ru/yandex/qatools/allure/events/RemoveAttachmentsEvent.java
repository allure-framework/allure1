package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.Step;

import java.util.Iterator;
import java.util.regex.Pattern;

import static ru.yandex.qatools.allure.utils.AllureResultsUtils.deleteAttachment;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 30.04.14
 */
public class RemoveAttachmentsEvent implements StepEvent {

    public final Pattern pattern;

    public RemoveAttachmentsEvent(String regex) {
        pattern = Pattern.compile(regex);
    }

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
