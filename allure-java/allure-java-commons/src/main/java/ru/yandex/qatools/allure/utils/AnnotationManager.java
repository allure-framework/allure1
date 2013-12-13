package ru.yandex.qatools.allure.utils;

import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
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

    public void isTitleAnnotationPresentUpdate(TestSuiteResult testSuite) {
        if (isAnnotationPresent(Title.class)) {
            testSuite.setTitle(getAnnotation(Title.class).value());
        }
    }

    public void isTitleAnnotationPresentUpdate(TestCaseResult testCase) {
        if (isAnnotationPresent(Title.class)) {
            testCase.setTitle(getAnnotation(Title.class).value());
        }
    }

    public void isDescriptionAnnotationPresentUpdate(TestSuiteResult testSuite) {
        if (isAnnotationPresent(Description.class)) {
            testSuite.setDescription(getAnnotation(Description.class).value());
        }
    }

    public void isDescriptionAnnotationPresentUpdate(TestCaseResult testCase) {
        if (isAnnotationPresent(Description.class)) {
            testCase.setDescription(getAnnotation(Description.class).value());
        }
    }

    public void isSeverityAnnotationPresentUpdate(TestCaseResult testCase) {
        if (isAnnotationPresent(Severity.class)) {
            testCase.setSeverity(getAnnotation(Severity.class).value());
        }
    }

    public void isStoryAnnotationPresentUpdate(TestCaseResult testCase) {
        if (isAnnotationPresent(Story.class)) {
            testCase.getLabels().addAll(getBehaviorLabels());
        }
    }

    public void isStoryAnnotationPresentUpdate(TestSuiteResult testSuite) {
        if (isAnnotationPresent(Story.class)) {
            testSuite.getLabels().addAll(getBehaviorLabels());
        }
    }


    private List<Label> getBehaviorLabels() {
        List<Label> labels = new ArrayList<>();
        for (Class<?> clazz : getAnnotation(Story.class).value()) {
            if (isBehaviorClass(clazz)) {
                labels.add(getStoryLabel(clazz));
                labels.add(getFeatureLabel(clazz.getDeclaringClass()));
            }
        }
        return labels;
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
