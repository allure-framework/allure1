package ru.yandex.qatools.allure.annotations;

import ru.yandex.qatools.allure.model.AttachmentType;

import java.lang.annotation.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.24.13
 * @see ru.yandex.qatools.allure.annotations.Attachment
 * @deprecated
 */
@Deprecated
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Attach {

    AttachmentType type() default AttachmentType.TXT;

    String name() default "{method}";

    String suffix() default ".txt";

}
