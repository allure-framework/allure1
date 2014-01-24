package ru.yandex.qatools.allure.e2e;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 1/24/14
 */
public class AllureCommonPageTest {

    @Test
    public void sampleTitleTest() {
        WebDriver driver = new PhantomJSDriver(DesiredCapabilities.firefox());
        driver.get("http://0.0.0.0:8080/e2e");
        assertThat(driver.getTitle(), equalTo("Allure Dashboard"));
    }
}
