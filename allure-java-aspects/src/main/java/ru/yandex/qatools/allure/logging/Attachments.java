package ru.yandex.qatools.allure.logging;

import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.config.AllureConfig;
import ru.yandex.qatools.allure.events.MakeAttachmentEvent;

/**
 * Created by Mihails Volkovs on 2015.03.06.
 */
public class Attachments {

    private static AllureConfig allureConfig = AllureConfig.newInstance();

    public static void addLogAttachment() {
        String attachment = TestStepLogs.removeLog();
        if (!"".equals(attachment)) {
            Allure allure = Allure.LIFECYCLE;
            allure.fire(new MakeAttachmentEvent(attachment.getBytes(allureConfig.getAttachmentsEncoding()), "log.txt", "text/plain"));
        }
    }
}
