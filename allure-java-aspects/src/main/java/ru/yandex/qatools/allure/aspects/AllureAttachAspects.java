package ru.yandex.qatools.allure.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.annotations.Attachment;
import ru.yandex.qatools.allure.AllureConfig;
import ru.yandex.qatools.allure.events.MakeAttachmentEvent;

import java.nio.charset.Charset;

import static ru.yandex.qatools.allure.aspects.AllureAspectUtils.*;

/**
 * Aspects (AspectJ) for handling {@link ru.yandex.qatools.allure.annotations.Attachment}.
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.10.13
 */
@Aspect
public class AllureAttachAspects {

    private static Allure ALLURE = Allure.LIFECYCLE;

    /**
     * Pointcut for things annotated with {@link ru.yandex.qatools.allure.annotations.Attachment}
     */
    @Pointcut("@annotation(ru.yandex.qatools.allure.annotations.Attachment)")
    public void withAttachmentAnnotation() {
        //pointcut body, should be empty
    }

    /**
     * Pointcut for any methods
     */
    @Pointcut("execution(* *(..))")
    public void anyMethod() {
        //pointcut body, should be empty
    }

    /**
     * Process data returned from method annotated with {@link ru.yandex.qatools.allure.annotations.Attachment}
     * If returned data is not a byte array, then use toString() method, and get bytes from it using
     * {@link AllureConfig#getAttachmentsEncoding()}
     */
    @AfterReturning(pointcut = "anyMethod() && withAttachmentAnnotation()", returning = "result")
    public void attachment(JoinPoint joinPoint, Object result) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Attachment attachment = methodSignature.getMethod().getAnnotation(Attachment.class);
        int maxLength = ALLURE.getConfig().getMaxTitleLength();
        String attachTitle = getTitle(
                maxLength, methodSignature.getName(), joinPoint.getThis(),
                joinPoint.getArgs(), attachment.value()
        );

        Charset charset = ALLURE.getConfig().getAttachmentsEncoding();
        byte[] bytes = (result instanceof byte[]) ? (byte[]) result : result.toString().getBytes(charset);
        ALLURE.fire(new MakeAttachmentEvent(bytes, attachTitle, attachment.type()));
    }

    /**
     * For tests only.
     */
    static void setAllure(Allure allure) {
        AllureAttachAspects.ALLURE = allure;
    }
}
