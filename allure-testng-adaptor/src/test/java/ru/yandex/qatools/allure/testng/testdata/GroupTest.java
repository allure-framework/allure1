package ru.yandex.qatools.allure.testng.testdata;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.testng.Assert.fail;

import org.testng.annotations.*;

public class GroupTest {

    @Test
    public void activeTest() { }

    @Test(enabled = false)
    public void inactiveTest() { }

    @Test(groups = "include")
    public void activeIncludedTest() { }

    @Test(groups = "include", enabled = false)
    public void inactiveIncludedTest() { }
    
    @Test(groups = "exclude")
    public void activeExcludedTest() { }

    @Test(groups = "exclude", enabled = false)
    public void inactiveExcludedTest() { }

    @Test(groups = {"include", "exclude"})
    public void activeIncludedExcludedTest() { }

    @Test(groups = {"include", "exclude"}, enabled = false)
    public void inactiveIncludedExcludedTest() { }
}
