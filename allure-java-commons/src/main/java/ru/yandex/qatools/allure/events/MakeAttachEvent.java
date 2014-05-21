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
        Attachment attachment = new Attachment();

        String type = convertType(getAttachmentType());
        String source = writeSafety(getAttach(), type);

        attachment.setSource(source);
        attachment.setTitle(getTitle());
        attachment.setType(type);

        step.getAttachments().add(attachment);
    }

    private static String write(Object attachment, String type) throws IOException {
        if (attachment instanceof String) {
            return writeAttachment(((String) attachment).getBytes(Charsets.UTF_8), type);
        } else if (attachment instanceof File) {
            byte[] bytes = FileUtils.readFileToByteArray((File) attachment);
            return writeAttachment(bytes, type);
        }
        throw new AllureException("Attach-method should be return 'java.lang.String' or 'java.io.File'.");
    }

    private static String writeSafety(Object attachment, String type) {
        try {
            return write(attachment, type);
        } catch (Exception e) {
            return writeAttachmentWithErrorMessage(e);
        }
    }

    private static String convertType(AttachmentType type) {
        switch (type) {
            case XML:
                return "text/xml";
            case HTML:
                return "text/html";
            case PNG:
                return "image/png";
            case JPG:
                return "image/jpeg";
            case JSON:
                return "application/json";
            case TXT:
                return "text/plain";
            default:
                return "*/*";
        }
    }

}
