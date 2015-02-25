package ru.yandex.qatools.allure.data.plugins;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 18.02.15
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@BindingAnnotation
public @interface PluginClassLoader {
}
