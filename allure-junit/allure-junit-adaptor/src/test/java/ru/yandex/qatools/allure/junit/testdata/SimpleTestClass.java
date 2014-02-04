package ru.yandex.qatools.allure.junit.testdata;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.fail;
import static org.junit.Assume.assumeTrue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 20.01.14
 */
public class SimpleTestClass {
    @Test
    public void passedTest() throws Exception {
    }

    @Test
    public void failedTest() throws Exception {
        fail();
    }


    @Test
    public void skippedTest() throws Exception {
        assumeTrue(false);
    }

    @Test
    public void brokenTest() throws Exception {
        throw new Exception();
    }

    @Ignore
    @Test
    public void ignoredTest() throws Exception {
    }
}
