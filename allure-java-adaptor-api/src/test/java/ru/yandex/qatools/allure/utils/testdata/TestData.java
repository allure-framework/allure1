package ru.yandex.qatools.allure.utils.testdata;

import ru.yandex.qatools.allure.model.Attachment;
import ru.yandex.qatools.allure.model.Step;

import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.01.16
 */
public final class TestData {

    TestData() {
    }

    public static String randomUid() {
        return randomAlphabetic(10);
    }

    public static String randomName() {
        return randomAlphabetic(10);
    }

    public static String randomTitle() {
        return randomAlphabetic(10);
    }

    public static Step randomStep() {
        return new Step().withName(randomName());
    }

    public static byte[] randomAttachmentBody() {
        return randomAlphabetic(100).getBytes(StandardCharsets.UTF_8);
    }

    public static Attachment randomAttachment(String source) {
        return new Attachment().withTitle(randomName()).withSource(source);
    }
}
