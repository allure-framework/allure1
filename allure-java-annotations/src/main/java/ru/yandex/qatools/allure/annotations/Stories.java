package ru.yandex.qatools.allure.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * In order to group your tests by stories simply annotate test suite
 * or test case with {@link ru.yandex.qatools.allure.annotations.Stories}
 * annotation. This annotation can take {@link ru.yandex.qatools.allure.annotations.Story}
 * array because one test case can relate to several stories:
 * <pre>
 * &#064;Stories({
 *     &#064;Story("MYPROJECT-1"),
 *     &#064;Story("MYPROJECT-2")
 * })
 * &#064;Test
 * public void myTest() {
 *     ...
 * }
 *
 * &#064;Story("Story")
 * &#064;Test
 * public void myTest() {
 *     ...
 * }
 * </pre>
 * @author Dmitry Baev charlie@yandex-team.ru
 * @author Sergey Korol serhii.s.korol@gmail.com
 *         Date: 10.07.16
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Stories {

    Story[] value();

}
