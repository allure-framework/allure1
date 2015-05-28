package ru.yandex.qatools.allure.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * You can use this annotation to add parameters to your tests:
 * <pre>
 * &#064;Parameter("My Param")
 * private String myParameter;
 *
 * &#064;Test
 * public void myTest() throws Exception {
 *      myParameter = "first";
 *      myParameter = "second";
 *      myParameter = "third";
 * }
 * </pre>
 * All three values will be added to report
 *
 * Note that the initializations of constant fields (static final fields
 * where the initializer is a constant string object or primitive value)
 * are not join points, since Java requires their references to be inlined.
 *
 * value - it's name of parameter, field name by default
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 19.06.14
 *
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Parameter {

    String value() default "";

}
