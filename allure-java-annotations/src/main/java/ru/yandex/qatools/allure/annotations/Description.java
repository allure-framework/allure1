package ru.yandex.qatools.allure.annotations;

import ru.yandex.qatools.allure.model.DescriptionType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Using this annotation you can add some text description
 * to test suite or test case.
 * <pre>
 * &#064;Test
 * &#064;Description("This is an example of my test")
 * public void myTest() throws Exception {
 *      ...
 * }
 * </pre>
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.24.13
 * @see ru.yandex.qatools.allure.model.DescriptionType
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Description {

    String value();

    DescriptionType type() default DescriptionType.TEXT;

}
