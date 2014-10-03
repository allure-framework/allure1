package ru.yandex.qatools.allure.utils;

import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;
import ru.yandex.qatools.allure.events.TestSuiteStartedEvent;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.SeverityLevel;

import java.lang.annotation.Annotation;
import java.util.*;

import static ru.yandex.qatools.allure.config.AllureModelUtils.*;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.12.13
 *         <p/>
 *         Util, using to collect information from class and method annotations
 */
public class AnnotationManager {

    private Map<Class<? extends Annotation>,Annotation> annotations = new HashMap<Class<? extends Annotation>, Annotation>();

    /**
     * Construct AnnotationManager using given annotations
     *
     * @param annotations initial value for annotations
     */
    public AnnotationManager(Collection<Annotation> annotations) {
        populateAnnotations(annotations);
    }

    /**
     * Construct AnnotationManager using given annotations
     *
     * @param annotations initial value for annotations
     */
    public AnnotationManager(Annotation... annotations) {
        populateAnnotations(Arrays.asList(annotations));
    }
    
    /**
     * Used to populate Map with given annotations
     *
     * @param annotations initial value for annotations
     */
    private void populateAnnotations(Collection<Annotation> annotations){
        for (Annotation each : annotations){
    	    this.annotations.put(each.annotationType(), each);
        }
    }
    
    /**
     * Set default values for annotations.
     * Initial annotation take precedence over the default annotation when both annotation types are present
     *
     * @param annotations default value for annotations 
     */
    public void setDefaults(Annotation[] defaultAnnotations){
    	if (defaultAnnotations == null) {
            return;
        }
    	for (Annotation each : defaultAnnotations){
    	    Class<? extends Annotation> key = each.annotationType();
            if (Title.class.equals(key) || Description.class.equals(key)){
                continue;
            }
            if (!annotations.containsKey(key)){
                annotations.put(key,each);
            }
        }
    } 
    
    /**
     * Sets into specified {@link ru.yandex.qatools.allure.events.TestSuiteStartedEvent}
     * information from Allure annotations.
     *
     * @param event to change
     */
    public void update(TestSuiteStartedEvent event) {
        if (isTitleAnnotationPresent()) {
            event.setTitle(getTitle());
        }

        if (isDescriptionAnnotationPresent()) {
            event.setDescription(getDescription());
        }

        if (isIssueAnnotationPresent()) {
            event.getLabels().add(createIssueLabel(getIssueKey()));
        }

        if (isIssuesAnnotationPresent()) {
            for (String issueKey: getIssueKeys()){
                event.getLabels().add(createIssueLabel(issueKey));
            }
        }

        event.getLabels().addAll(getStoryLabels());
        event.getLabels().addAll(getFeatureLabels());
    }

    /**
     * Sets into specified {@link ru.yandex.qatools.allure.events.TestCaseStartedEvent}
     * information from Allure annotations.
     *
     * @param event to change
     */
    public void update(TestCaseStartedEvent event) {
        if (isTitleAnnotationPresent()) {
            event.setTitle(getTitle());
        }

        if (isDescriptionAnnotationPresent()) {
            event.setDescription(getDescription());
        }

        if (isSeverityAnnotationPresent()) {
            event.getLabels().add(createSeverityLabel(getSeverity()));
        }
        
        if (isIssueAnnotationPresent()) {
            event.getLabels().add(createIssueLabel(getIssueKey()));
        }
        
        if (isIssuesAnnotationPresent()) {
            for (String issueKey: getIssueKeys()){
                event.getLabels().add(createIssueLabel(issueKey));
            }
        }

        event.getLabels().addAll(getStoryLabels());
        event.getLabels().addAll(getFeatureLabels());
    }

    /**
     * @return true if {@link ru.yandex.qatools.allure.annotations.Title}
     * annotation present in {@link #annotations} and false otherwise
     */
    public boolean isTitleAnnotationPresent() {
        return isAnnotationPresent(Title.class);
    }

    /**
     * @return true if {@link ru.yandex.qatools.allure.annotations.Description}
     * annotation present in {@link #annotations} and false otherwise
     */
    public boolean isDescriptionAnnotationPresent() {
        return isAnnotationPresent(Description.class);
    }

    /**
     * @return true if {@link ru.yandex.qatools.allure.annotations.Severity}
     * annotation present in {@link #annotations} and false otherwise
     */
    public boolean isSeverityAnnotationPresent() {
        return isAnnotationPresent(Severity.class);
    }

    /**
     * @return true if {@link ru.yandex.qatools.allure.annotations.Stories}
     * annotation present in {@link #annotations} and false otherwise
     */
    public boolean isStoriesAnnotationPresent() {
        return isAnnotationPresent(Stories.class);
    }

    /**
     * @return true if {@link ru.yandex.qatools.allure.annotations.Features}
     * annotation present in {@link #annotations} and false otherwise
     */
    public boolean isFeaturesAnnotationPresent() {
        return isAnnotationPresent(Features.class);
    }
    
    /**
     * @return true if {@link ru.yandex.qatools.allure.annotations.Issues}
     * annotation present in {@link #annotations} and false otherwise
     */
    public boolean isIssuesAnnotationPresent() {
        return isAnnotationPresent(Issues.class);
    }
    
    /**
     * @return true if {@link ru.yandex.qatools.allure.annotations.Issue}
     * annotation present in {@link #annotations} and false otherwise
     */
    public boolean isIssueAnnotationPresent() {
        return isAnnotationPresent(Issue.class);
    }

    /**
     * Find first {@link ru.yandex.qatools.allure.annotations.Title} annotation
     *
     * @return title or null if annotation doesn't present
     */
    public String getTitle() {
        Title title = getAnnotation(Title.class);
        return title == null ? null : title.value();
    }

    /**
     * Find first {@link ru.yandex.qatools.allure.annotations.Description} annotation
     *
     * @return {@link ru.yandex.qatools.allure.model.Description} or null if
     * annotation doesn't present
     */
    public ru.yandex.qatools.allure.model.Description getDescription() {
        Description description = getAnnotation(Description.class);
        return description == null ? null : new ru.yandex.qatools.allure.model.Description()
                .withValue(description.value())
                .withType(description.type());
    }

    /**
     * Find first {@link ru.yandex.qatools.allure.annotations.Severity} annotation
     *
     * @return {@link ru.yandex.qatools.allure.model.SeverityLevel} or null if
     * annotation doesn't present
     */
    public SeverityLevel getSeverity() {
        Severity severity = getAnnotation(Severity.class);
        return severity == null ? null : severity.value();
    }

    /**
     * Find first {@link ru.yandex.qatools.allure.annotations.Issue} annotation
     *
     * @return issue key or null if annotation is not present
     */
    public String getIssueKey() {
        Issue issue = getAnnotation(Issue.class);
        return issue == null ? null : issue.value();
    }

    /**
     * Construct label for all {@link ru.yandex.qatools.allure.annotations.Stories} annotations
     * using {@link ru.yandex.qatools.allure.config.AllureModelUtils#createStoryLabel(String)}
     *
     * @return {@link java.util.List} of created labels
     */
    public List<Label> getStoryLabels() {
        if (!isAnnotationPresent(Stories.class)) {
            return Collections.emptyList();
        }

        List<Label> result = new ArrayList<>();
        for (String story : getAnnotation(Stories.class).value()) {
            result.add(createStoryLabel(story));
        }
        return result;
    }

    /**
     * Construct label for all {@link ru.yandex.qatools.allure.annotations.Features} annotations
     * using {@link ru.yandex.qatools.allure.config.AllureModelUtils#createFeatureLabel(String)}
     *
     * @return {@link java.util.List} of created labels
     */
    public List<Label> getFeatureLabels() {
        if (!isAnnotationPresent(Features.class)) {
            return Collections.emptyList();
        }

        List<Label> result = new ArrayList<>();
        for (String feature : getAnnotation(Features.class).value()) {
            result.add(createFeatureLabel(feature));
        }
        return result;
    }

    /**
     * Find {@link ru.yandex.qatools.allure.annotations.Issues} annotation and return respective key
     *
     * @return issue keys or empty array if annotation isn't present
     */
    public String[] getIssueKeys() {
        Issues issues = getAnnotation(Issues.class);
        if (issues == null) {
            return new String[0];
        }
        List<String> keys = new ArrayList<>();
        for (Issue issue: issues.value()){
            keys.add(issue.value());
        }
        return keys.toArray(new String[keys.size()]);
    }

    /**
     * Returns true if {@link #annotations} contains annotation with specified type,
     * false otherwise
     *
     * @param annotationType annotation type to find
     * @return true if {@link #annotations} contains annotation with specified type,
     * false otherwise
     */
    public <T extends Annotation> boolean isAnnotationPresent(Class<T> annotationType) {
        return annotations.containsKey(annotationType);
    }

    /**
     * Find annotation with specified type into {@link #annotations}
     *
     * @param annotationType annotation type to find
     * @return the first found annotation with specified type, of null if
     * {@link #annotations} doesn't contains such
     */
    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        Annotation value = annotations.get(annotationType);
        return annotationType.cast(value);
    }


}
