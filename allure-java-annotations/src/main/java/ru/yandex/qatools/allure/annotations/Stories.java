package ru.yandex.qatools.allure.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * In order to group your tests by stories simply annotate test suite
 * or test case with {@link ru.yandex.qatools.allure.annotations.Stories}
 * annotation. This annotation can take either one string or a string
 * array because one test case can relate to several stories:
 * <pre>
 * &#064;Stories({"Story1", "Story2"})
 * &#064;Test
 * public void myTest() {
 *     ...
 * }
 *
 * &#064;Stories("Story")
 * &#064;Test
 * public void myTest() {
 *     ...
 * }
 * </pre>
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 25.12.13
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Stories {

    String[] value();

}
