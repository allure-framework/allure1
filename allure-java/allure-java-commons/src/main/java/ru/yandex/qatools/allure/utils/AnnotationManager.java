package ru.yandex.qatools.allure.utils;

import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;
import ru.yandex.qatools.allure.events.TestSuiteStartedEvent;
import ru.yandex.qatools.allure.model.*;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.12.13
 */
public class AnnotationManager {

    private Collection<Annotation> annotations;

    public AnnotationManager(Collection<Annotation> annotations) {
        this.annotations = annotations;
    }

    public AnnotationManager(Annotation... annotations) {
        this.annotations = Arrays.asList(annotations);
    }

    public void update(TestSuiteStartedEvent event) {
        if (isTitleAnnotationPresent()) {
            event.setTitle(getTitle());
        }

        if (isDescriptionAnnotationPresent()) {
            event.setDescription(getDescription());
        }

        if (isStoriesAnnotationPresent()) {
            event.getLabels().addAll(getStoryLabels());
        }

        if (isFeaturesAnnotationPresent()) {
            event.getLabels().addAll(getFeatureLabels());
        }
    }

    public void update(TestCaseStartedEvent event) {
        if (isTitleAnnotationPresent()) {
            event.setTitle(getTitle());
        }

        if (isDescriptionAnnotationPresent()) {
            event.setDescription(getDescription());
        }

        if (isStoriesAnnotationPresent()) {
            event.getLabels().addAll(getStoryLabels());
        }

        if (isFeaturesAnnotationPresent()) {
            event.getLabels().addAll(getFeatureLabels());
        }

        if (isSeverityAnnotationPresent()) {
            event.setSeverity(getSeverity());
        }
    }

    public boolean isTitleAnnotationPresent() {
        return isAnnotationPresent(Title.class);
    }

    public boolean isDescriptionAnnotationPresent() {
        return isAnnotationPresent(Description.class);
    }

    public boolean isSeverityAnnotationPresent() {
        return isAnnotationPresent(Severity.class);
    }

    public boolean isStoriesAnnotationPresent() {
        return isAnnotationPresent(Stories.class);
    }

    public boolean isFeaturesAnnotationPresent() {
        return isAnnotationPresent(Features.class);
    }

    public String getTitle() {
        return getAnnotation(Title.class).value();
    }

    public ru.yandex.qatools.allure.model.Description getDescription() {
        Description description = getAnnotation(Description.class);
        return new ru.yandex.qatools.allure.model.Description()
                .withValue(description.value())
                .withType(description.type());
    }

    public SeverityLevel getSeverity() {
        return getAnnotation(Severity.class).value();
    }

    public List<Label> getStoryLabels() {
        List<Label> result = new ArrayList<>();
        for (String story : getAnnotation(Stories.class).value()) {
            result.add(new Label().withName("story").withValue(story));
        }
        return result;
    }

    public List<Label> getFeatureLabels() {
        List<Label> result = new ArrayList<>();
        for (String feature : getAnnotation(Features.class).value()) {
            result.add(new Label().withName("feature").withValue(feature));
        }
        return result;
    }

    public <T extends Annotation> boolean isAnnotationPresent(Class<T> annotationType) {
        for (Annotation each : annotations) {
            if (each.annotationType().equals(annotationType)) {
                return true;
            }
        }
        return false;
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        for (Annotation each : annotations) {
            if (each.annotationType().equals(annotationType)) {
                return annotationType.cast(each);
            }
        }
        return null;
    }


}
