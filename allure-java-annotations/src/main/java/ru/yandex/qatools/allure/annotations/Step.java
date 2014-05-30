package ru.yandex.qatools.allure.annotations;

import java.lang.annotation.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.24.13
 *         <p/>
 *         In order to define steps you need to annotate respective methods
 *         with @Step annotation. When not specified step name equals to
 *         annotated method name converted to human readable format. To
 *         define explicit step name:
 *         {@code
 *         @Step("Open '{0}' page.")
 *         public void openPageByAddress(String pageAddress) {
 *             ...
 *         }
 *         }
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Step {

    String value() default "";

}
