package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Description;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;

import java.util.Arrays;

/**
 * Using to start new testCase
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 *         <p/>
 * @see ru.yandex.qatools.allure.Allure
 */
public class TestCaseStartedEvent extends AbstractTestCaseStartedEvent {

    /**
     * Constructs an new event with specified suiteUid and name
     *
     * @param suiteUid initial suiteUid
     * @param name     initial name
     */
    public TestCaseStartedEvent(String suiteUid, String name) {
        setSuiteUid(suiteUid);
        setName(name);
    }

    /**
     * Sets to testCase start time, default status, name, title, description and labels
     *
     * @param testCase to change
     */
    @Override
    public void process(TestCaseResult testCase) {
        testCase.setStart(System.currentTimeMillis());
        testCase.setStatus(Status.PASSED);
        testCase.setName(getName());
        testCase.setTitle(getTitle());
        testCase.setDescription(getDescription());
        testCase.setLabels(getLabels());
    }

    /**
     * Sets title using fluent-api
     *
     * @param title value to set
     * @return modified instance
     */
    public TestCaseStartedEvent withTitle(String title) {
        setTitle(title);
        return this;
    }

    /**
     * Sets description using fluent-api
     *
     * @param description value to set
     * @return modified instance
     */
    public TestCaseStartedEvent withDescription(Description description) {
        setDescription(description);
        return this;
    }

    /**
     * Sets labels using fluent-api
     *
     * @param label  value to set
     * @param labels other values to set
     * @return modified instance
     */
    public TestCaseStartedEvent withLabels(Label label, Label... labels) {
        getLabels().add(label);
        getLabels().addAll(Arrays.asList(labels));
        return this;
    }
}
