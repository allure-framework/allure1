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
public class TestSuiteStartedEvent implements TestSuiteEvent {
    private String uid;
    private String name;

    private String title;
    private Description description;
    private List<Label> labels = new ArrayList<>();

    public TestSuiteStartedEvent(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    @Override
    public void process(TestSuiteResult testSuite) {
        testSuite.setStart(System.currentTimeMillis());
        testSuite.setName(name);
        testSuite.setTitle(title);
        testSuite.setDescription(description);
        testSuite.setLabels(labels);
    }

    @Override
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Description getDescription() {
        return description;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public TestSuiteStartedEvent withTitle(String title) {
        setTitle(title);
        return this;
    }

    public TestSuiteStartedEvent withDescription(Description description) {
        setDescription(description);
        return this;
    }

    public TestSuiteStartedEvent withLabels(List<Label> labels) {
        setLabels(labels);
        return this;
    }

    public TestSuiteStartedEvent withLabels(Label... labels) {
        setLabels(Arrays.asList(labels));
        return this;
    }
}
