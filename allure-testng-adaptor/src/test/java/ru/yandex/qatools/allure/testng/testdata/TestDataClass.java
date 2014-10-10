package ru.yandex.qatools.allure.testng.testdata;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
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

    @DataProvider
    public Object[][] dataProvider() {
        return new Object[][] {
                {1}, {2}, {3}
        };
    }

    @Test(dataProvider = "dataProvider")
    public void parametrizedTest(int parameter) {
        assertThat(parameter, equalTo(2));
    }
    
    @Test(enabled = false, description = "This is pending test")
    public void pendingTest(){ 
    }
}
