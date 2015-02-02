package ru.yandex.qatools.allure.annotations;

import java.lang.annotation.*;

/**
 * Use this annotation to link a test method with a test case in Test Management System (TMS). Usage:
 * <pre>
 * &#064;TestCaseId("MYPROJECT-1")
 * public void myTest() {
 *     ...
 * }
 * </pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TestCaseId {

    String value();

}
