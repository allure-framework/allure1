package ru.yandex.qatools.allure.junit.testdata;

import org.junit.Test;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.TestCaseEvent;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 28.01.15
 */
public class TestWithTimeoutAnnotation {

    public static final String NAME = "TestWithTimeoutRule#title";

    @Test(timeout = 10000)
    public void someTest() throws Exception {
        Allure.LIFECYCLE.fire(new TestCaseEvent() {
            @Override
            public void process(TestCaseResult context) {
                context.setTitle(NAME);
            }
        });
    }
}
