package ru.yandex.qatools.allure.testng;

import org.testng.ITestResult;
import org.testng.SkipException;

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

    public Throwable getThrowable() {
        Throwable throwable = iTestResult.getThrowable();
        if (throwable != null) {
            return throwable;
        }

        switch (iTestResult.getStatus()) {
            case ITestResult.SKIP:
                return new SkipException("The test was skipped for some reason");
            default:
                return null;
        }
    }

    public String getName() {
        return iTestResult.getName();
    }
}
