package ru.yandex.qatools.allure.utils;

import org.junit.Before;
import org.junit.Test;
import ru.yandex.qatools.allure.annotations.FeatureClass;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.annotations.StoryClass;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;
import ru.yandex.qatools.allure.events.TestSuiteStartedEvent;
import ru.yandex.qatools.allure.model.Description;
import ru.yandex.qatools.allure.model.DescriptionType;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.SeverityLevel;
import ru.yandex.qatools.allure.utils.testdata.SimpleClass;

import java.lang.annotation.Annotation;
import java.util.List;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
        assertTrue(annotationManager.isBehavior());
        assertTrue(annotationManager.isStoryAnnotationPresent());
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
    public void testBehaviorLabelsGetter() throws Exception {
        checkLabels(annotationManager.getBehaviorLabels());
    }

    @Test
    public void testUpdateTestSuiteStartedEvent() throws Exception {
        TestSuiteStartedEvent event = new TestSuiteStartedEvent("some.uid", "some.name");
        annotationManager.update(event);

        assertThat(event.getTitle(), equalTo("some.title"));

        Description description = annotationManager.getDescription();
        assertThat(description.getValue(), is("some.description"));
        assertThat(description.getType(), is(DescriptionType.TEXT));

        checkLabels(event.getLabels());
    }

    @Test
    public void testUpdateTestCaseStartedEvent() throws Exception {
        TestCaseStartedEvent event = new TestCaseStartedEvent("some.uid", "some.name");
        annotationManager.update(event);

        assertThat(event.getTitle(), equalTo("some.title"));

        Description description = annotationManager.getDescription();
        assertThat(description.getValue(), is("some.description"));
        assertThat(description.getType(), is(DescriptionType.TEXT));

        assertThat(event.getSeverity(), equalTo(SeverityLevel.BLOCKER));

        checkLabels(event.getLabels());

    }

    private void checkLabels(List<Label> labels) {
        assertThat(labels, hasSize(2));
        assertThat(extract(labels, on(Label.class).getName()), contains(StoryClass.LABEL_NAME, FeatureClass.LABEL_NAME));

        for (Label label : labels) {
            if (label.getName().equals(StoryClass.LABEL_NAME)) {
                assertThat(label.getValue(), equalTo("some.story"));
            }
            if (label.getName().equals(FeatureClass.LABEL_NAME)) {
                assertThat(label.getValue(), equalTo("some.feature"));
            }
        }
    }
}
