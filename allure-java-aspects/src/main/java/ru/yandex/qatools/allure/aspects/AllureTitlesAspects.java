package ru.yandex.qatools.allure.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.events.TestCaseEvent;
import ru.yandex.qatools.allure.model.TestCaseResult;

import static ru.yandex.qatools.allure.aspects.AllureAspectUtils.getTitle;

/**
 * @author Elena Kudryashova
 */
@Aspect
public class AllureTitlesAspects {
    private static Allure ALLURE = Allure.LIFECYCLE;

    @Pointcut("@annotation(ru.yandex.qatools.allure.annotations.Title)")
    public void withTitleAnnotation() {
        // Pointcut body, should be empty.
    }

    @Pointcut("execution(* *(..))")
    public void anyMethod() {
        // Pointcut body, should be empty.
    }

    @Before("anyMethod() && withTitleAnnotation()")
    public void testCaseStart(JoinPoint joinPoint) {
        final String testCaseTitle = createTitle(joinPoint);
        ALLURE.fire(new TestCaseEvent() {
            @Override
            public void process(TestCaseResult testCaseResult) {
                testCaseResult.setTitle(testCaseTitle);
            }
        });
    }

    public String createTitle(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Title title = methodSignature.getMethod().getAnnotation(Title.class);
        return title == null ? "" : getTitle(title.value(), methodSignature.getName(), joinPoint.getThis(), joinPoint.getArgs());
    }

    /**
     * For tests only
     */
    static void setAllure(Allure allure) {
        AllureTitlesAspects.ALLURE = allure;
    }
}
