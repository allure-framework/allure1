package ru.yandex.qatools.allure.events;

import org.apache.commons.lang3.ArrayUtils;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.Arrays;

import static ru.yandex.qatools.allure.utils.AllureWriteUtils.humanize;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestSuiteStartedEvent implements TestSuiteEvent {
    private String uid;
    private String name;

    private String title;
    private String description;
    private Label[] labels;

    public TestSuiteStartedEvent(String uid, String name) {
        this.uid = uid;
        this.name = name;
    }

    @Override
    public void process(TestSuiteResult testSuite) {
        testSuite.setStart(System.currentTimeMillis());
        testSuite.setName(name);

        if (title != null) {
            testSuite.setTitle(title);
        } else {
            testSuite.setTitle(humanize(name));
        }

        if (description != null) {
            testSuite.setDescription(description);
        }

        if (labels != null) {
            testSuite.getLabels().addAll(Arrays.asList(labels));
        }
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Label[] getLabels() {
        return labels;
    }

    public void setLabels(Label... labels) {
        this.labels = labels;
    }

    public void addLabels(Label... labels) {
        this.labels = ArrayUtils.addAll(this.labels, labels);
    }

    public TestSuiteStartedEvent withTitle(String title) {
        setTitle(title);
        return this;
    }

    public TestSuiteStartedEvent withDescription(String description) {
        setDescription(description);
        return this;
    }

    public TestSuiteStartedEvent withLabels(Label... labels) {
        setLabels(labels);
        return this;
    }
}
