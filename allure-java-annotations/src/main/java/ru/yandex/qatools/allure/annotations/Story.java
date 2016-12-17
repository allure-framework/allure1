package ru.yandex.qatools.allure.annotations;

import java.lang.annotation.*;

/**
 * Use this annotation to link a single story from issue tracker to test cases and test suites. Usage:
 * <pre>
 * &#064;Story("MYPROJECT-1")
 * public void myTest() {
 *     ...
 * }
 * </pre>
 *
 * @author Sergey Korol serhii.s.korol@gmail.com
 *         Date: 10.07.16
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Story {

    String value();

}
