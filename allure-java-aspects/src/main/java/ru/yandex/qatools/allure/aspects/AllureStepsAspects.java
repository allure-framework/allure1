package ru.yandex.qatools.allure.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.events.StepFailureEvent;
import ru.yandex.qatools.allure.events.StepFinishedEvent;
import ru.yandex.qatools.allure.events.StepStartedEvent;
import ru.yandex.qatools.allure.logging.Attachments;
import ru.yandex.qatools.allure.logging.TestStepLogs;

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
        TestStepLogs.addLog();
        ALLURE.fire(startedEvent);
    }

    @AfterThrowing(pointcut = "anyMethod() && withStepAnnotation()", throwing = "e")
    public void stepFailed(JoinPoint joinPoint, Throwable e) {
        ALLURE.fire(new StepFailureEvent().withThrowable(e));
        finishStep();
    }

    @AfterReturning(pointcut = "anyMethod() && withStepAnnotation()", returning = "result")
    public void stepStop(JoinPoint joinPoint, Object result) {
        finishStep();
    }

    private void finishStep() {
        Attachments.addLogAttachment();
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
