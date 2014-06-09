package ru.yandex.qatools.allure.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.annotations.Attach;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.config.AllureConfig;
import ru.yandex.qatools.allure.events.MakeAttachEvent;
import ru.yandex.qatools.allure.events.MakeAttachmentEvent;

import java.nio.charset.Charset;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.10.13
 */

@Aspect
public class AllureAttachAspects {

    @Deprecated
    @Pointcut("@annotation(ru.yandex.qatools.allure.annotations.Attach)")
    public void withAttachAnnotation() {
        //pointcut body, should be empty
    }

    @Pointcut("@annotation(ru.yandex.qatools.allure.annotations.Attachment)")
    public void withAttachmentAnnotation() {
        //pointcut body, should be empty
    }

    @Pointcut("execution(* *(..))")
    public void anyMethod() {
        //pointcut body, should be empty
    }

    @SuppressWarnings("deprecation")
    @Deprecated
    @AfterReturning(pointcut = "anyMethod() && withAttachAnnotation()", returning = "result")
    public void attach(JoinPoint joinPoint, Object result) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Attach attach = methodSignature.getMethod().getAnnotation(Attach.class);
        String attachTitle = AllureAspectUtils.getTitle(attach.name(), methodSignature.getName(), joinPoint.getArgs());
        Allure.LIFECYCLE.fire(new MakeAttachEvent(attachTitle, attach.type(), result));
    }

    @AfterReturning(pointcut = "anyMethod() && withAttachmentAnnotation()", returning = "result")
    public void attachment(JoinPoint joinPoint, Object result) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Attachment attachment = methodSignature.getMethod().getAnnotation(Attachment.class);
        String attachTitle = AllureAspectUtils.getTitle(
                attachment.value(),
                methodSignature.getName(),
                joinPoint.getArgs()
        );

        Charset charset = AllureConfig.newInstance().getAttachmentsEncoding();
        byte[] bytes = (result instanceof byte[]) ? (byte[]) result : result.toString().getBytes(charset);
        Allure.LIFECYCLE.fire(new MakeAttachmentEvent(bytes, attachTitle, attachment.type()));
    }

}
