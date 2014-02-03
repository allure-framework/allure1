package ru.yandex.qatools.allure.testng;

import org.testng.ITestResult;
import org.testng.SkipException;

import java.lang.annotation.Annotation;

/**
 * Wraps {@link ITestResult} to provide more convenient methods for Allure framework.
 *
 * @author Kirill Kozlov kozlov.k.e@gmail.com
 *         Date: 04.02.14
 */
public class AllureTestResultAdaptor {

    private ITestResult iTestResult;

    public AllureTestResultAdaptor(ITestResult iTestResult) {
        this.iTestResult = iTestResult;
    }

    public Annotation[] getAnnotations() {
        return iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotations();
    }

    public Throwable getThrowable() {
        Throwable throwable = iTestResult.getThrowable();
        if (throwable == null) {
            if (iTestResult.getStatus() == ITestResult.SKIP) {
                throwable = new SkipException("The test was skipped for some reason");
            }
        }
        return throwable;
    }

    public String getName() {
        return iTestResult.getName();
    }
}
