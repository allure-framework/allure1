package ru.yandex.qatools.allure.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.annotations.Attach;
import ru.yandex.qatools.allure.events.MakeAttachEvent;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.10.13
 */

@Aspect
public class AllureAttachAspects {

	@Pointcut("@annotation(ru.yandex.qatools.allure.annotations.Attach)")
	public void withAttachAnnotation() {
	}

	@Pointcut("execution(* *(..))")
	public void anyMethod() {
	}

	@AfterReturning(pointcut = "anyMethod() && withAttachAnnotation()", returning = "result")
	public void attach(JoinPoint joinPoint, Object result) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Attach attach = methodSignature.getMethod().getAnnotation(Attach.class);
		String attachTitle = AllureAspectUtils.getTitle(attach.name(), methodSignature.getName(), joinPoint.getArgs());
        Allure.LIFECYCLE.fire(new MakeAttachEvent(attachTitle, attach.type(), result));
	}
}
