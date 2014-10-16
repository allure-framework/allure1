package ru.yandex.qatools.allure.junit.testdata;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.qatools.allure.annotations.Title;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 16.10.14
 */
public class TestClassWithExceptionInBefore {

    @BeforeClass
    public static void beforeClass() throws Exception {
        throw new Exception("Exception if before class");
    }

    @Title("Some test title")
    @Test
    public void someTest() throws Exception {
    }
}
