package ru.yandex.qatools.allure.e2e.tabs;

import com.google.common.base.Joiner;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.yandex.qatools.allure.JSErrorsRule;

import java.text.MessageFormat;

import static org.openqa.selenium.support.ui.ExpectedConditions.textToBePresentInElement;


public class XUnitTabTest {

    @Rule
    public JSErrorsRule jsErrorsRule = new JSErrorsRule();

    String testrunPane = ".pane_col:first-child";
    String testsuitePane = ".pane_col:nth-child(2)";
    String paneHeader = ".pane__header h3";
    String itemRow = "[allure-table] tbody tr:nth-child({0})";

    Joiner joiner = Joiner.on(" ").skipNulls();
    WebDriver driver;

    @Before
    public void openBrowser() throws Exception {
        driver = jsErrorsRule.getDriver();
        WebDriverWait wait = new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".tab-content")));
    }

    @Test
    public void openTestsuiteTest() throws Exception {
        driver.findElement(
            By.cssSelector(joiner.join(testrunPane, MessageFormat.format(itemRow, 2)))
        ).click();
        WebDriverWait wait = new WebDriverWait(driver, 3);

        wait.until(ExpectedConditions.not(textToBePresentInElement(
            By.cssSelector(joiner.join(testsuitePane, paneHeader)),
            "{{testsuite.title}}"
        )));
    }
}
