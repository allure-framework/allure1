package ru.yandex.qatools.allure.junit.testdata;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.TestCaseEvent;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 28.01.15
 */
public class TestWithTimeoutRule {

    @Rule
    public Timeout timeout = new Timeout(10000);

    @Test
    public void someTest() throws Exception {
        Allure.LIFECYCLE.fire(new TestCaseEvent() {
            @Override
            public void process(TestCaseResult context) {
                context.setTitle(TestWithTimeoutAnnotation.NAME);
            }
        });
    }
}
