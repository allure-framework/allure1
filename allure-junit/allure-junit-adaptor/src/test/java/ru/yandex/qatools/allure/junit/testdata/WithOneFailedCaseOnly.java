package ru.yandex.qatools.allure.junit.testdata;

import org.junit.Test;

import static org.junit.Assert.fail;

/**
 * User: lanwen
 * Date: 30.04.14
 * Time: 23:07
 */
public class WithOneFailedCaseOnly {

    @Test
    public void onlyOneFailedTestMethod() throws Exception {
        fail("Run, Forest! Run!");
    }
}
