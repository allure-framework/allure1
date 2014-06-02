package ru.yandex.qatools.allure.annotations;

import ru.yandex.qatools.allure.model.SeverityLevel;

import java.lang.annotation.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.24.13
 *         <p/>
 *         Each test has severity level. You can change test case severity
 *         lavel using this annotation.
 * @see ru.yandex.qatools.allure.model.SeverityLevel
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Severity {

    SeverityLevel value();

}
