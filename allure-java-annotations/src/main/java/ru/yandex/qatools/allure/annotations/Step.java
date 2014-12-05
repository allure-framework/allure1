package ru.yandex.qatools.allure.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * In order to define steps you need to annotate respective methods
 * with @Step annotation. When not specified step name equals to
 * annotated method name converted to human readable format. To
 * define explicit step name:
 * <pre>
 * &#064;Step("Open '{0}' page.")
 * public void openPageByAddress(String pageAddress) {
 *     ...
 * }
 * </pre>
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.24.13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Step {

    String value() default "";

}
