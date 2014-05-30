package ru.yandex.qatools.allure.annotations;

import java.lang.annotation.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 25.12.13
 *         <p/>
 *         In order to group your tests by stories simply annotate test suite
 *         or test case with {@link ru.yandex.qatools.allure.annotations.Stories}
 *         annotation. This annotation can take either one string or a string
 *         array because one test case can relate to several stories:
 *         {@code
 *         @Stories({"Story1", "Story2"})
 *         @Test
 *         public void myTest() {
 *             ...
 *         }
 *
 *         @Stories("Story")
 *         @Test
 *         public void myTest() {
 *             ...
 *         }
 *         }
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Stories {

    String[] value();

}
