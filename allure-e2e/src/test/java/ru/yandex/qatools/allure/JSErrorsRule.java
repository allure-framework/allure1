package ru.yandex.qatools.allure;

import com.google.common.io.Resources;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static org.junit.Assert.assertEquals;

public class JSErrorsRule extends TestWatcher {

    private WebDriver driver;
    private AllureEnvironment environment = AllureEnvironment.newInstance();

    private String loggingJS;


    public JSErrorsRule() {
        try {
            loggingJS = Resources.toString(getResource("log.js"), UTF_8);
        } catch (IOException e) {
            loggingJS = "";
        }
    }

    public WebDriver driver() {
        return driver;
    }

    @Override
    protected void starting(Description description) {
        driver = new PhantomJSDriver(new DesiredCapabilities());
        driver.manage().window().setSize(new Dimension(1024, 768));
        driver.get(environment.getBaseUrl());
        ((JavascriptExecutor) driver).executeScript(loggingJS);
    }

    @Override
    protected void succeeded(Description description) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        assertEquals("Should be no errors in javascript",
                executor.executeScript("return window.allureErrors.length"), 0L);
    }

    @Override
    protected void finished(Description description) {
        driver.quit();
    }
}
