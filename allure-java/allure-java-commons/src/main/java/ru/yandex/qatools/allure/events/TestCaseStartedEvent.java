package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestCaseStartedEvent extends AbstractTestCaseStartedEvent {

    public TestCaseStartedEvent(String suiteUid, String name) {
        setSeverity(SeverityLevel.NORMAL);
        setSuiteUid(suiteUid);
        setName(name);
    }

    @Override
    public void process(TestCaseResult testCase) {
        testCase.setStart(System.currentTimeMillis());
        testCase.setStatus(Status.PASSED);
        testCase.setName(getName());
        testCase.setSeverity(getSeverity());
        testCase.setTitle(getTitle());
        testCase.setDescription(getDescription());
        testCase.setLabels(getLabels());
    }

}
