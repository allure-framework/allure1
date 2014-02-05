package ru.yandex.qatools.allure.e2e;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.AllureEnvironment;

import static org.junit.Assert.assertEquals;

public class TabsTest {

    public AllureEnvironment environment = AllureEnvironment.newInstance();

    WebDriver driver;

    @Before
    public void setupWebdriver() {
        driver = new PhantomJSDriver(new DesiredCapabilities());
    }


    @Before
    public void openBrowser() throws Exception {
        driver.get(environment.getBaseUrl());
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tab-content")));
    }

    public void checkHash(String expectedHash) {
        String url = driver.getCurrentUrl();
        String hash = url.substring(url.indexOf('#'));
        assertEquals(expectedHash, hash);
    }

    @Test
    public void defectsTab() throws Exception {
        driver.findElement(By.cssSelector(".b-vert__icon.glyphicon-flag")).click();
        checkHash("#/defects");
    }

    @Test
    public void homeTab() throws Exception {
        checkHash("#/home");
    }

    @Test
    public void behaviorsTab() throws Exception {
        driver.findElement(By.cssSelector(".b-vert__icon.glyphicon-list")).click();
        checkHash("#/features");
    }

    @Test
    public void graphTab() throws Exception {
        driver.findElement(By.cssSelector(".b-vert__icon.glyphicon-stats")).click();
        checkHash("#/graph");
    }

    @Test
    public void timelineTab() throws Exception {
        driver.findElement(By.cssSelector(".b-vert__icon.glyphicon-time")).click();
        checkHash("#/timeline");
    }

    @After
    public void quit() {
        driver.quit();
    }

}
