package ru.yandex.qatools.allure.junit.testdata;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * User: lanwen
 * Date: 30.03.14
 * Time: 22:42
 */
public class AssertionErrorInBeforeClass {

    @BeforeClass
    public static void bcWithException() {
        fail("Some assertion");
    }

    @Test
    public void emptyTest() throws Exception {
    }
}
