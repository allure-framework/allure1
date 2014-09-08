package ru.yandex.qatools.allure.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 01.08.14
 *
 *         <p>Use this annotation to link multiple issues from issue tracker to test cases and test suites. Usage:
 *         {@code
 *         @Issues({
 *             @Issue("MYPROJECT-1"),
 *             @Issue("MYPROJECT-2")
 *         })
 *         public void myTest(){
 *             ...
 *         }
 *         }
 *             
 *         </p>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Issues {

    Issue[] value();

}
