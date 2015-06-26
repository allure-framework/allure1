package ru.yandex.qatools.allure.testng.testdata;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

/**
 * TestNg Class For testing Configuration method skip behavior 
 * 
 * @author Michael Braiman braimanm@gmail.com
 *         Date: 15.06.15
 */
public class ConfigMethodsTest {

    @BeforeSuite
    public void configBeforeSuite(XmlTest test) {
        if (test.getName().equals("BeforeSuite")) {
            throw new RuntimeException();
        }
    }

    @BeforeTest
    public void configBeforeTest(XmlTest test) {
        if (test.getName().equals("BeforeTest")) {
            throw new RuntimeException();
        }
    }

    @BeforeClass
    public void configBeforeClass(XmlTest test) {
        if (test.getName().equals("BeforeClass")) {
            throw new RuntimeException();
        }
    }

    @BeforeGroups(groups = {"group1"})
    public void configBeforeGroups(XmlTest test) {
        if (test.getName().equals("BeforeGroups")) {
            throw new RuntimeException();
        }
    }

    @BeforeMethod
    public void configBeforeMethod(XmlTest test) {
        if (test.getName().equals("BeforeMethod")) {
            throw new RuntimeException();
        }
    }

    @Test(groups = {"group1"})
    public void test() {
        Assert.assertTrue(true);
    }

    @AfterMethod
    public void configAfterMethod(XmlTest test) {
        if (test.getName().equals("AfterMethod")) {
            throw new RuntimeException();
        }
    }

    @AfterGroups(groups = {"group1"})
    public void configAfterGroups(XmlTest test) {
        if (test.getName().equals("AfterGroups")) {
            throw new RuntimeException();
        }
    }

    @AfterClass
    public void configAfterClass(XmlTest test) {
        if (test.getName().equals("AfterClass")) {
            throw new RuntimeException();
        }
    }

    @AfterTest
    public void configAfterTest(XmlTest test) {
        if (test.getName().equals("AfterTest")) {
            throw new RuntimeException();
        }
    }

    @AfterSuite
    public void configAfterSuite(XmlTest test) {
        if (test.getName().equals("AfterSuite")) {
            throw new RuntimeException();
        }
    }

}
