package ru.yandex.qatools.allure.utils;

import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;
import ru.yandex.qatools.allure.events.TestSuiteStartedEvent;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.SeverityLevel;

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

        if (isBehavior()) {
            event.addLabels(getBehaviorLabels());
        }
    }

    public void update(TestCaseStartedEvent event) {
        if (isTitleAnnotationPresent()) {
            event.setTitle(getTitle());
        }

        if (isDescriptionAnnotationPresent()) {
            event.setDescription(getDescription());
        }

        if (isBehavior()) {
            event.addLabels(getBehaviorLabels());
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

    public boolean isStoryAnnotationPresent() {
        return isAnnotationPresent(Story.class);
    }

    public boolean isSeverityAnnotationPresent() {
        return isAnnotationPresent(Severity.class);
    }

    public boolean isBehavior() {
        return isStoryAnnotationPresent() && isBehaviorClasses(getAnnotation(Story.class).value());
    }

    public String getTitle() {
        return getAnnotation(Title.class).value();
    }

    public String getDescription() {
        return getAnnotation(Description.class).value();
    }

    public SeverityLevel getSeverity() {
        return getAnnotation(Severity.class).value();
    }

    public Label[] getBehaviorLabels() {
        List<Label> labels = new ArrayList<>();
        for (Class<?> clazz : getAnnotation(Story.class).value()) {
            if (isBehaviorClass(clazz)) {
                labels.add(getStoryLabel(clazz));
                labels.add(getFeatureLabel(clazz.getDeclaringClass()));
            }
        }
        return labels.toArray(new Label[labels.size()]);
    }

    public Label getFeatureLabel(Class<?> clazz) {
        return new Label()
                .withName(FeatureClass.LABEL_NAME)
                .withValue(clazz.getAnnotation(FeatureClass.class).value());
    }

    public Label getStoryLabel(Class<?> clazz) {
        return new Label()
                .withName(StoryClass.LABEL_NAME)
                .withValue(clazz.getAnnotation(StoryClass.class).value());
    }

    public boolean isBehaviorClasses(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            if(!isBehaviorClass(clazz)) {
                return false;
            }
        }
        return true;
    }

    public boolean isBehaviorClass(Class<?> clazz) {
        return clazz.isAnnotationPresent(StoryClass.class)
                && clazz.getDeclaringClass().isAnnotationPresent(FeatureClass.class);
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
