package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.Collection;
import java.util.Iterator;

/**
 * User: eoff (eoff@yandex-team.ru)
 * Date: 31.07.14
 */
public class TestCasesFilterStatusEvent extends AbstractTestSuiteFinishedEvent {
    private Collection<Status> toHide;

    public TestCasesFilterStatusEvent(Collection<Status> toHide) {
        this.toHide = toHide;
    }

    @Override
    public void process(TestSuiteResult testSuite) {
        if (toHide.isEmpty()) {
            return;
        }
        Iterator<TestCaseResult> iterator = testSuite.getTestCases().listIterator();
        while (iterator.hasNext()) {
            TestCaseResult result = iterator.next();
            if (toHide.contains(result.getStatus())) {
                iterator.remove();
            }
        }
    }
}
