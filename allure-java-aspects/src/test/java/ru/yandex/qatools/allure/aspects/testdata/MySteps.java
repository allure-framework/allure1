package ru.yandex.qatools.allure.aspects.testdata;

import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.annotations.Step;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 08.12.14
 */
public class MySteps {

    @Step
    public void sampleStep() {
    }

    @Step
    public static void sampleStaticStep() {
    }

    @Step
    public void failedStep(Exception throwable) throws Exception {
        throw throwable;
    }

    @Step("Tata title")
    public void stepWithTitle() {
    }

    @Attachment
    public byte[] byteAttachment(byte[] attachment) {
        return attachment;
    }

    @Attachment(type = "super-type", value = "super-title")
    public String stringAttachment(String attachment) {
        return attachment;
    }

    @Attachment("{method}: {0}")
    public String stringAttachment(String message, String attachment) {
        return attachment;
    }
}
