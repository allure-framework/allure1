package ru.yandex.qatools.allure.utils;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;
import ru.yandex.qatools.allure.events.TestSuiteStartedEvent;
import ru.yandex.qatools.allure.model.*;
import ru.yandex.qatools.allure.utils.testdata.SimpleClass;

import java.lang.annotation.Annotation;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static ru.yandex.qatools.allure.config.AllureModelUtils.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.12.13
 */
public class AnnotationManagerTest {

    private AnnotationManager annotationManager;

    @Before
    public void setUpCaseAnnotations() throws Exception {
        Annotation[] annotations = SimpleClass.class.getMethod("simpleMethod").getAnnotations();
        annotationManager = new AnnotationManager(annotations);

    }

    @Test
    public void testIsAnnotationPresentMethods() throws Exception {
        assertTrue(annotationManager.isTitleAnnotationPresent());
        assertTrue(annotationManager.isSeverityAnnotationPresent());
        assertTrue(annotationManager.isStoriesAnnotationPresent());
        assertTrue(annotationManager.isFeaturesAnnotationPresent());
        assertTrue(annotationManager.isDescriptionAnnotationPresent());
        assertFalse(annotationManager.isAnnotationPresent(Step.class));
    }

    @Test
    public void testTitleValueGetter() {
        assertThat(annotationManager.getTitle(), is("some.title"));
    }

    @Test
    public void testDescriptionValueGetter() throws Exception {
        Description description = annotationManager.getDescription();
        assertThat(description.getValue(), is("some.description"));
        assertThat(description.getType(), is(DescriptionType.TEXT));
    }

    @Test
    public void testSeverityValueGetter() throws Exception {
        assertThat(annotationManager.getSeverity(), is(SeverityLevel.BLOCKER));
    }

    @Test
    public void testStoryLabelsGetter() throws Exception {
        List<Label> labels = annotationManager.getStoryLabels();
        assertThat(labels, hasSize(1));
        assertEquals(labels.get(0), createLabel(LabelName.STORY, "some.story"));
    }

    @Test
    public void testFeaturesLabelsGetter() throws Exception {
        List<Label> labels = annotationManager.getFeatureLabels();
        assertThat(labels, hasSize(1));
        assertEquals(labels.get(0), createLabel(LabelName.FEATURE, "some.feature"));
    }

    @Test
    public void testUpdateTestSuiteStartedEvent() throws Exception {
        TestSuiteStartedEvent event = new TestSuiteStartedEvent("some.uid", "some.name");
        annotationManager.update(event);

        assertThat(event.getTitle(), equalTo("some.title"));

        Description description = annotationManager.getDescription();
        assertThat(description.getValue(), is("some.description"));
        assertThat(description.getType(), is(DescriptionType.TEXT));

        assertTrue(event.getLabels().contains(createStoryLabel("some.story")));
        assertTrue(event.getLabels().contains(createFeatureLabel("some.feature")));
        assertFalse(event.getLabels().contains(createSeverityLabel(SeverityLevel.BLOCKER)));
    }

    @Test
    public void testUpdateTestCaseStartedEvent() throws Exception {
        TestCaseStartedEvent event = new TestCaseStartedEvent("some.uid", "some.name");
        annotationManager.update(event);

        assertThat(event.getTitle(), equalTo("some.title"));

        Description description = annotationManager.getDescription();
        assertThat(description.getValue(), is("some.description"));
        assertThat(description.getType(), is(DescriptionType.TEXT));

        assertTrue(event.getLabels().contains(createStoryLabel("some.story")));
        assertTrue(event.getLabels().contains(createFeatureLabel("some.feature")));
        assertTrue(event.getLabels().contains(createSeverityLabel(SeverityLevel.BLOCKER)));
    }
}
