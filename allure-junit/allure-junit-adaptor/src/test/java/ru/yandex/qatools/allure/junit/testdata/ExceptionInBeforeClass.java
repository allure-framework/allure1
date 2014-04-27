package ru.yandex.qatools.allure.junit.testdata;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * User: lanwen
 * Date: 30.03.14
 * Time: 22:42
 */
public class ExceptionInBeforeClass {

    @BeforeClass
    public static void bcWithException() {
        throw exc();
    }

    @Test
    public void emptyTest() throws Exception {
    }

    public static RuntimeException exc() {
        return new RuntimeException("Some exception");
    }
}
