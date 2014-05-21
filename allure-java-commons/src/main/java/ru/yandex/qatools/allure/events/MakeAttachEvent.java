package ru.yandex.qatools.allure.events;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import ru.yandex.qatools.allure.exceptions.AllureException;
import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.AttachmentType;
import ru.yandex.qatools.allure.model.Step;

import java.io.File;
import java.io.IOException;

import static ru.yandex.qatools.allure.utils.AllureResultsUtils.writeAttachment;
import static ru.yandex.qatools.allure.utils.AllureResultsUtils.writeAttachmentWithErrorMessage;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
@Deprecated
public class MakeAttachEvent extends AbstractMakeAttachEvent {

    public MakeAttachEvent(String title, AttachmentType attachmentType, Object attach) {
        setTitle(title);
        setAttachmentType(attachmentType);
        setAttach(attach);
    }

    @Override
    public void process(Step step) {
        Attachment attachment = writeSafety(getAttach(), getTitle());

        step.getAttachments().add(attachment);
    }

    private static Attachment write(Object attachment, String title) throws IOException {
        if (attachment instanceof String) {
            return writeAttachment(((String) attachment).getBytes(Charsets.UTF_8), title);
        } else if (attachment instanceof File) {
            byte[] bytes = FileUtils.readFileToByteArray((File) attachment);
            return writeAttachment(bytes, title);
        }
        throw new AllureException("Attach-method should be return 'java.lang.String' or 'java.io.File'.");
    }

    private static Attachment writeSafety(Object attachment, String title) {
        try {
            return write(attachment, title);
        } catch (Exception e) {
            return writeAttachmentWithErrorMessage(e, title);
        }
    }
}
