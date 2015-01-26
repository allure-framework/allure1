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

import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
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
    
    private AnnotationManager setAnnotationManager(String method) throws Exception{
        Annotation[] annotations = SimpleClass.class.getMethod(method).getAnnotations();
        Annotation[] defaultAnnotations = SimpleClass.class.getAnnotations();
        annotationManager = new AnnotationManager(annotations);
        annotationManager.setDefaults(defaultAnnotations);
        return annotationManager;
    }
    
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
        assertTrue(annotationManager.isIssueAnnotationPresent());
        assertTrue(annotationManager.isIssuesAnnotationPresent());
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

        assertThat(event.getLabels(), not(hasItems(
                createSeverityLabel(SeverityLevel.BLOCKER)
        )));
        assertThat(event.getLabels(), hasItems(
                createStoryLabel("some.story"), 
                createFeatureLabel("some.feature"),
                createIssueLabel("some.simple.issue"),
                createIssueLabel("some.nested.issue.1"),
                createIssueLabel("some.nested.issue.2")
        ));   
    }

    @Test
    public void testUpdateTestCaseStartedEvent() throws Exception {
        TestCaseStartedEvent event = new TestCaseStartedEvent("some.uid", "some.name");
        annotationManager.update(event);

        assertThat(event.getTitle(), equalTo("some.title"));

        Description description = annotationManager.getDescription();
        assertThat(description.getValue(), is("some.description"));
        assertThat(description.getType(), is(DescriptionType.TEXT));

        assertThat(event.getLabels(), hasItems(
                createStoryLabel("some.story"), 
                createFeatureLabel("some.feature"),
                createSeverityLabel(SeverityLevel.BLOCKER),
                createIssueLabel("some.simple.issue"),
                createIssueLabel("some.nested.issue.1"),
                createIssueLabel("some.nested.issue.2")
        ));        
    }
    
    @Test
    public void testInitialValuesUpdateTestCaseStartedEvent() throws Exception {
        AnnotationManager annotationManager = setAnnotationManager("simpleMethod");

        TestCaseStartedEvent event = new TestCaseStartedEvent("some.uid", "some.name");
        annotationManager.update(event);

        assertThat(event.getTitle(), equalTo("some.title"));

        Description description = annotationManager.getDescription();
        assertThat(description.getValue(), is("some.description"));
        assertThat(description.getType(), is(DescriptionType.TEXT));

        assertThat(event.getLabels(), hasItems(
                createStoryLabel("some.story"), 
                createFeatureLabel("some.feature"),
                createSeverityLabel(SeverityLevel.BLOCKER),
                createIssueLabel("some.simple.issue"),
                createIssueLabel("some.nested.issue.1"),
                createIssueLabel("some.nested.issue.2"),
                createTestLabel("test.case.id")
        ));
    }

    @Test
    public void testDefaultValuesUpdateTestCaseStartedEvent() throws Exception {
        AnnotationManager annotationManager = setAnnotationManager("defaultMethod");

        TestCaseStartedEvent event = new TestCaseStartedEvent("some.uid", "some.name");
        annotationManager.update(event);

        assertThat(event.getTitle(), is(nullValue()));
        Description description = annotationManager.getDescription();
        assertThat(description, is(nullValue()));
        assertThat(event.getLabels(), hasItems(
                createStoryLabel("default.story"), 
                createFeatureLabel("default.feature"),
                createIssueLabel("default.issue")
        ));
    }
    
    @Test
    public void testCombinedValuesUpdateTestCaseStartedEvent() throws Exception {
        AnnotationManager annotationManager = setAnnotationManager("combinedMethod");

        TestCaseStartedEvent event = new TestCaseStartedEvent("some.uid", "some.name");
        annotationManager.update(event);
       
        assertThat(event.getTitle(), is(nullValue()));
        Description description = annotationManager.getDescription();
        assertThat(description, is(nullValue()));
        assertThat(event.getLabels(), hasItems(
                createStoryLabel("default.story"), 
                createFeatureLabel("default.feature"),
                createSeverityLabel(SeverityLevel.CRITICAL),
                createIssueLabel("initial.issue")
        ));
    }
    
}
