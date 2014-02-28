package ru.yandex.qatools.allure.aspects;

import org.junit.Test;

import static ru.yandex.qatools.allure.aspects.AllureAspectUtils.getTitle;

/**
 * User: hrundelb
 * Date: 27.02.14
 * Time: 21:50
 */
public class AllureAspectUtilsTest {

    @Test
    public void getTitleTest() {
        Object[] objects = {new String[]{"la la la", "bla bla bla"}, 25, 'a', 8000L};
        System.out.println(getTitle("{method} string: {0} - {1} - {2} - {3}", "step", objects));
    }
}
