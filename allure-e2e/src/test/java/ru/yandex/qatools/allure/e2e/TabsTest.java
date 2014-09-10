package ru.yandex.qatools.allure.e2e;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import ru.yandex.qatools.allure.JSErrorsRule;
import ru.yandex.qatools.allure.Page;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static ru.yandex.qatools.allure.Helpers.existsAndVisible;
import static ru.yandex.qatools.allure.UrlFragmentMatcher.hasFragment;
import static ru.yandex.qatools.matchers.decorators.MatcherDecorators.should;
import static ru.yandex.qatools.matchers.decorators.MatcherDecorators.timeoutHasExpired;

public class TabsTest {

    @Rule
    public JSErrorsRule rule = new JSErrorsRule();

    private Page page;

    @Before
    public void openBrowser() throws Exception {
        page = new Page(rule.driver());
        assertThat(page.tabContent(), should(existsAndVisible()).whileWaitingUntil(timeoutHasExpired(SECONDS.toMillis(3))));
    }

    public void checkHash(String expectedHash) {
        assertThat(rule.driver(), should(hasFragment(expectedHash)).whileWaitingUntil(timeoutHasExpired(SECONDS.toMillis(3))));
    }

    @Test
    public void defectsTab() throws Exception {
        page.tabs().defects().click();
        checkHash("/defects");
    }

    @Test
    public void overviewTab() throws Exception {
        page.tabs().overview().click();
        checkHash("/");
    }

    @Test
    public void homeTab() throws Exception {
        checkHash("/home");
    }

    @Test
    public void behaviorsTab() throws Exception {
        page.tabs().behaviours().click();
        checkHash("/features");
    }

    @Test
    public void graphTab() throws Exception {
        page.tabs().graph().click();
        checkHash("/graph");
    }

    @Test
    public void timelineTab() throws Exception {
        page.tabs().timeline().click();
        checkHash("/timeline");
    }

}
