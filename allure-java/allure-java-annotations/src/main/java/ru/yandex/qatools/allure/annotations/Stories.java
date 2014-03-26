package ru.yandex.qatools.allure.annotations;

import java.lang.annotation.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 25.12.13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Stories {

    String[] value();

}
