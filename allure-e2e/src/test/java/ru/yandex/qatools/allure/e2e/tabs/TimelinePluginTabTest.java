package ru.yandex.qatools.allure.e2e.tabs;


import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import ru.yandex.qatools.allure.JSErrorsRule;
import ru.yandex.qatools.allure.Page;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.yandex.qatools.allure.Helpers.existsAndVisible;
import static ru.yandex.qatools.matchers.decorators.MatcherDecorators.should;
import static ru.yandex.qatools.matchers.decorators.MatcherDecorators.timeoutHasExpired;

public class TimelinePluginTabTest {

    @Rule
    public JSErrorsRule jsErrorsRule = new JSErrorsRule();

    private Page page;

    @Before
    public void openBrowser() throws Exception {
        page = new Page(jsErrorsRule.driver());
        page.tabs().timeline().click();
        assertThat(page.timelineTab().barAt(0), should(existsAndVisible())
                .whileWaitingUntil(timeoutHasExpired(SECONDS.toMillis(3))));
    }


    @Test
    public void shouldNotSeeAnOpenTestcase() throws Exception {
        assertThat(page.timelineTab().testcasePane(), should(not(existsAndVisible()))
                .whileWaitingUntil(timeoutHasExpired(SECONDS.toMillis(3))));
    }

    @Test
    public void shouldOpenTestcaseOnClick() throws Exception {
        page.timelineTab().barAt(0).click();

        assertThat(page.timelineTab().testcasePane(), should(existsAndVisible())
                .whileWaitingUntil(timeoutHasExpired(SECONDS.toMillis(3))));
    }
}
