package ru.yandex.qatools.allure.annotations;

import java.lang.annotation.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.24.13
 *         <p/>
 *         Use this annotation to set test suite and test case title.
 *         Tilte overrides default name in report.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Title {

    String value();
}