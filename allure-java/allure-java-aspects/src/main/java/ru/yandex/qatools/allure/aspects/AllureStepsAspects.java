package ru.yandex.qatools.allure.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.annotations.Step;
import ru.yandex.qatools.allure.events.StepFailureEvent;
import ru.yandex.qatools.allure.events.StepStartEvent;
import ru.yandex.qatools.allure.events.StepStopEvent;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.10.13
 */
@SuppressWarnings("unused")
@Aspect
public class AllureStepsAspects {

	@Pointcut("@annotation(ru.yandex.qatools.allure.annotations.Step)")
	public void withStepAnnotation() {
	}

	@Pointcut("execution(* *(..))")
	public void anyMethod() {
	}

	@Before("anyMethod() && withStepAnnotation()")
	public void stepStart(JoinPoint joinPoint) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Step step = methodSignature.getMethod().getAnnotation(Step.class);
		String stepTitle = AllureAspectUtils.getTitle(step.value(), methodSignature.getName(), joinPoint.getArgs());
        Allure.LIFECYCLE.fire(new StepStartEvent(stepTitle));
    }

	@AfterThrowing(pointcut = "anyMethod() && withStepAnnotation()", throwing = "e")
	public void stepFailed(JoinPoint joinPoint, Throwable e) {
        Allure.LIFECYCLE.fire(new StepFailureEvent(e));
        Allure.LIFECYCLE.fire(new StepStopEvent());
	}

	@AfterReturning(pointcut = "anyMethod() && withStepAnnotation()", returning = "result")
	public void stepStop(JoinPoint joinPoint, Object result) {
        Allure.LIFECYCLE.fire(new StepStopEvent());
    }
}
