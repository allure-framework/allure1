package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Description;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.Arrays;

/**
 * Using to start new testSuite
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 *         <p/>
 * @see ru.yandex.qatools.allure.Allure
 */
public class TestSuiteStartedEvent extends AbstractTestSuiteStartedEvent {

    /**
     * Constructs an new event with specified suiteUid and name
     *
     * @param uid  initial uid
     * @param name initial name
     */
    public TestSuiteStartedEvent(String uid, String name) {
        setUid(uid);
        setName(name);
    }

    /**
     * Sets to testSuite start time, name, title, description and labels
     *
     * @param testSuite to change
     */
    @Override
    public void process(TestSuiteResult testSuite) {
        testSuite.setStart(System.currentTimeMillis());
        testSuite.setName(getName());
        testSuite.setTitle(getTitle());
        testSuite.setDescription(getDescription());
        testSuite.setLabels(getLabels());
    }

    /**
     * Sets title using fluent-api
     *
     * @param title value to set
     * @return modified instance
     */
    public TestSuiteStartedEvent withTitle(String title) {
        setTitle(title);
        return this;
    }

    /**
     * Sets description using fluent-api
     *
     * @param description value to set
     * @return modified instance
     */
    public TestSuiteStartedEvent withDescription(Description description) {
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
    public TestSuiteStartedEvent withLabels(Label label, Label... labels) {
        getLabels().add(label);
        getLabels().addAll(Arrays.asList(labels));
        return this;
    }
}
