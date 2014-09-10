package ru.yandex.qatools.allure.annotations;

import java.lang.annotation.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 15.05.14
 *         <p/>
 *         A file with additional information captured during a test such
 *         as log, screenshot, log file, dump, server response and so on.
 *         When some test fails attachments help to understand the reason
 *         of failure faster.
 *         <p/>
 *         An attachment is simply a method annotated with
 *         {@link ru.yandex.qatools.allure.annotations.Attachment} returns
 *         either String or byte array which should be added to report:
 *         {@code
 *         @Attachment(value = "Page screenshot", type = "image/png")
 *         public byte[] saveScreenshot(byte[] screenShot) {
 *             return screenShot;
 *         }
 *         }
 *         <p/>
 * @since 1.4.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Attachment {

    /**
     *  Attachment name
     */
    String value() default "{method}";

    /**
     * Valid attachment MimeType, for example "text/plain" or "application/json"
     */
    String type() default "";
}
