package ru.yandex.qatools.allure.e2e;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.yandex.qatools.allure.AllureEnvironment;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 1/24/14
 */
public class CommonPageTest {

    public AllureEnvironment environment = AllureEnvironment.newInstance();

    @Test
    public void sampleTitleTest() {
        WebDriver driver = new PhantomJSDriver(DesiredCapabilities.firefox());
        driver.get(environment.getBaseUrl());
        assertThat(driver.getTitle(), equalTo("Allure Dashboard"));
    }
}
