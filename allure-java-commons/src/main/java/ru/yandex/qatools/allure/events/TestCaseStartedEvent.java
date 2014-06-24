package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Description;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.Parameter;
import ru.yandex.qatools.allure.model.ParameterKind;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 *         <p/>
 *         Using to start new testCase
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
        testCase.getParameters().addAll(getSystemProperties());
    }

    private List<Parameter> getSystemProperties() {
        List<Parameter> results = new ArrayList<>();
        Properties properties = System.getProperties();
        Enumeration<?> enumeration = System.getProperties().propertyNames();
        while (enumeration.hasMoreElements()) {
            try {
                String propertyName = (String) enumeration.nextElement();
                results.add(new Parameter()
                                .withName(propertyName)
                                .withValue(properties.getProperty(propertyName))
                                .withKind(ParameterKind.SYSTEM_PROPERTY)
                );
            } catch (Exception ignored) {
            }
        }
        return results;
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
