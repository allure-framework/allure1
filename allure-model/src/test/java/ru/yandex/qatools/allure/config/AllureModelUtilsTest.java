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

}
