package ru.yandex.qatools.allure.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 03.12.13
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface FeatureClass {
    public static final String LABEL_NAME = "feature";

    String value();
}
