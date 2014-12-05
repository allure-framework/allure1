package ru.yandex.qatools.allure.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Use this annotation to link multiple issues from issue tracker to test cases and test suites. Usage:
 * <p/>
 * <pre>
 * &#064;Issues({
 *     &#064;Issue("MYPROJECT-1"),
 *     &#064;Issue("MYPROJECT-2")
 * })
 * public void myTest(){
 *     ...
 * }
 * </pre>
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 01.08.14
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Issues {

    Issue[] value();

}
