package ru.yandex.qatools.allure.e2e;

import org.junit.Rule;
import org.junit.Test;
import ru.yandex.qatools.allure.JSErrorsRule;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 1/24/14
 */
public class CommonPageTest {

    @Rule
    public JSErrorsRule rule = new JSErrorsRule();

    @Test
    public void sampleTitleTest() {
        assertThat(rule.driver().getTitle(), equalTo("Allure Dashboard"));
    }
}
