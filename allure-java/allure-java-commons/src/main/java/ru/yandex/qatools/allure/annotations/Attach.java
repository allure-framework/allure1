package ru.yandex.qatools.allure.annotations;

import ru.yandex.qatools.allure.model.AttachmentType;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.24.13
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Attach {

    AttachmentType type() default AttachmentType.TXT;

    String name() default "{method}";

    String suffix() default ".txt";
}
