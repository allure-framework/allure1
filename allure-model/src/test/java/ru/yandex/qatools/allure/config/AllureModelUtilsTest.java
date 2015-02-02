package ru.yandex.qatools.allure.config;

import org.junit.Test;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.LabelName;
import ru.yandex.qatools.allure.model.SeverityLevel;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 28.05.14
 */
public class AllureModelUtilsTest {

    @Test(expected = IllegalStateException.class)
    public void createInstanceTest() throws Exception {
        new AllureModelUtils();
    }

    @Test
    public void createFeatureLabelTest() throws Exception {
        Label label = AllureModelUtils.createFeatureLabel("some-feature");
        assertThat(label.getValue(), is("some-feature"));
        assertThat(label.getName(), is(LabelName.FEATURE.value()));
    }

    @Test
    public void createStoryLabelTest() throws Exception {
        Label label = AllureModelUtils.createStoryLabel("some-story");
        assertThat(label.getValue(), is("some-story"));
        assertThat(label.getName(), is(LabelName.STORY.value()));
    }

    @Test
    public void createSeverityLabelTest() throws Exception {
        Label label = AllureModelUtils.createSeverityLabel(SeverityLevel.BLOCKER);
        assertThat(label.getValue(), is(SeverityLevel.BLOCKER.value()));
        assertThat(label.getName(), is(LabelName.SEVERITY.value()));
    }

    @Test
    public void createTestLabelTest() {
        Label label = AllureModelUtils.createTestLabel("some-test");
        assertThat(label.getValue(), is("some-test"));
        assertThat(label.getName(), is(LabelName.TEST_ID.value()));
    }

    @Test
    public void shouldCreateProgrammingLabel() throws Exception {
        Label label = AllureModelUtils.createProgrammingLanguageLabel();
        assertThat(label.getName(), is(LabelName.LANGUAGE.value()));
        assertThat(label.getValue(), is("JAVA"));
    }

    @Test
    public void shouldCreateFrameworkLabel() throws Exception {
        Label label = AllureModelUtils.createTestFrameworkLabel("framework");
        assertThat(label.getName(), is(LabelName.FRAMEWORK.value()));
        assertThat(label.getValue(), is("framework"));
    }

    @Test
    public void shouldCreateIssueLabel() throws Exception {
        Label label = AllureModelUtils.createIssueLabel("issue");
        assertThat(label.getName(), is(LabelName.ISSUE.value()));
        assertThat(label.getValue(), is("issue"));
    }

    @Test
    public void shouldCreateHostLabel() throws Exception {
        Label label = AllureModelUtils.createHostLabel("host");
        assertThat(label.getName(), is(LabelName.HOST.value()));
        assertThat(label.getValue(), is("host"));
    }

    @Test
    public void shouldCreateThreadLabel() throws Exception {
        Label label = AllureModelUtils.createThreadLabel("thread");
        assertThat(label.getName(), is(LabelName.THREAD.value()));
        assertThat(label.getValue(), is("thread"));
    }
}
