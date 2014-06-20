package ru.yandex.qatools.allure.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.FieldSignature;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.events.AddParameterEvent;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 19.06.14
 */
@Aspect
public class AllureParametersAspects {

    @Pointcut("@annotation(ru.yandex.qatools.allure.annotations.Parameter)")
    public void withParameterAnnotation() {
        //pointcut body, should be empty
    }

    @Pointcut("set(* *)")
    public void setValueToAnyField() {
        //pointcut body, should be empty
    }

    @After("setValueToAnyField() && withParameterAnnotation()")
    public void parameterValueChanged(JoinPoint joinPoint) {
        try {
            FieldSignature fieldSignature = (FieldSignature) joinPoint.getSignature();
            Parameter parameter = fieldSignature.getField().getAnnotation(Parameter.class);
            String name = parameter.value().isEmpty() ? fieldSignature.getName() : parameter.value();
            Allure.LIFECYCLE.fire(new AddParameterEvent(name, joinPoint.getArgs()[0].toString()));
        } catch (Exception ignored) {
        }
    }

}
