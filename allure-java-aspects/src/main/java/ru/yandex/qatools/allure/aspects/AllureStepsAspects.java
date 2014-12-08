package ru.yandex.qatools.allure.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.events.StepFailureEvent;
import ru.yandex.qatools.allure.events.StepFinishedEvent;
import ru.yandex.qatools.allure.events.StepStartedEvent;

import static ru.yandex.qatools.allure.aspects.AllureAspectUtils.getName;
import static ru.yandex.qatools.allure.aspects.AllureAspectUtils.getTitle;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.10.13
 */
@SuppressWarnings("unused")
@Aspect
public class AllureStepsAspects {

    private static Allure ALLURE = Allure.LIFECYCLE;

    @Pointcut("@annotation(ru.yandex.qatools.allure.annotations.Step)")
    public void withStepAnnotation() {
        //pointcut body, should be empty
    }

    @Pointcut("execution(* *(..))")
    public void anyMethod() {
        //pointcut body, should be empty
    }

    @Before("anyMethod() && withStepAnnotation()")
    public void stepStart(JoinPoint joinPoint) {
        String stepTitle = createTitle(joinPoint);

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        StepStartedEvent startedEvent = new StepStartedEvent(
                getName(methodSignature.getName(), joinPoint.getArgs())
        );

        if (!stepTitle.isEmpty()) {
            startedEvent.setTitle(stepTitle);
        }

        ALLURE.fire(startedEvent);
    }

    @AfterThrowing(pointcut = "anyMethod() && withStepAnnotation()", throwing = "e")
    public void stepFailed(JoinPoint joinPoint, Throwable e) {
        ALLURE.fire(new StepFailureEvent().withThrowable(e));
        ALLURE.fire(new StepFinishedEvent());
    }

    @AfterReturning(pointcut = "anyMethod() && withStepAnnotation()", returning = "result")
    public void stepStop(JoinPoint joinPoint, Object result) {
        ALLURE.fire(new StepFinishedEvent());
    }

    public String createTitle(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Step step = methodSignature.getMethod().getAnnotation(Step.class);
        return step == null ? "" : getTitle(step.value(), methodSignature.getName(), joinPoint.getThis(), joinPoint.getArgs());
    }

    /**
     * For tests only
     */
    static void setAllure(Allure allure) {
        AllureStepsAspects.ALLURE = allure;
    }
}
