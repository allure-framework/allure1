package ru.yandex.qatools.allure.testng.testdata;

import org.testng.annotations.Test;
import static org.testng.Assert.fail;

/**
 * Class with simple tests. It's used to check correctness of Allure TestNG listener.
 *
 * @author Kirill Kozlov kozlov.k.e@gmail.com
 *         Date: 04.02.14
 */
public class TestDataClass {

    @Test
    public void passedTest() { }

    @Test
    public void failedTest() {
        fail();
    }

    @Test(dependsOnMethods = "failedTest")
    public void skippedByDependencyTest() { }

}
