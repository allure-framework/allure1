package ru.yandex.qatools.allure.events;

import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.TestCaseResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 11.11.13
 */
public class TestCaseStartedEvent implements TestCaseEvent {

    private String suiteUid;
    private String name;

    private String title;
    private String description;
    private SeverityLevel severity = SeverityLevel.NORMAL;
    private List<Label> labels = new ArrayList<>();

    public TestCaseStartedEvent(String suiteUid, String name) {
        this.suiteUid = suiteUid;
        this.name = name;
    }

    @Override
    public void process(TestCaseResult testCase) {
        testCase.setStart(System.currentTimeMillis());
        testCase.setStatus(Status.PASSED);
        testCase.setName(name);
        testCase.setSeverity(severity);
        testCase.setTitle(title);
        testCase.setDescription(description);
        testCase.getLabels().addAll(labels);
    }

    public String getSuiteUid() {
        return suiteUid;
    }

    public void setSuiteUid(String suiteUid) {
        this.suiteUid = suiteUid;
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

    public SeverityLevel getSeverity() {
        return severity;
    }

    public void setSeverity(SeverityLevel severity) {
        this.severity = severity;
    }

    public List<Label> getLabels() {
        return labels;
    }

    public void setLabels(List<Label> labels) {
        this.labels = labels;
    }

    public TestCaseStartedEvent withTitle(String title) {
        setTitle(title);
        return this;
    }

    public TestCaseStartedEvent withDescription(String description) {
        setDescription(description);
        return this;
    }

    public TestCaseStartedEvent withSeverity(SeverityLevel severity) {
        setSeverity(severity);
        return this;
    }

    public TestCaseStartedEvent withLabels(Label... labels) {
        setLabels(Arrays.asList(labels));
        return this;
    }

    public TestCaseStartedEvent withLabels(List<Label> labels) {
        setLabels(labels);
        return this;
    }
}
