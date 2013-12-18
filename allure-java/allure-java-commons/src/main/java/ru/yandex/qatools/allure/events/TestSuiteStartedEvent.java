package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Description;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestSuiteStartedEvent extends AbstractTestSuiteStartedEvent {

    public TestSuiteStartedEvent(String uid, String name) {
        setUid(uid);
        setName(name);
    }

    @Override
    public void process(TestSuiteResult testSuite) {
        testSuite.setStart(System.currentTimeMillis());
        testSuite.setName(getName());
        testSuite.setTitle(getTitle());
        testSuite.setDescription(getDescription());
        testSuite.setLabels(getLabels());
    }
}
