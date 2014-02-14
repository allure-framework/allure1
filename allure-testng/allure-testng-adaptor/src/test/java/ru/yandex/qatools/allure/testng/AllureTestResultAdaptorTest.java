package ru.yandex.qatools.allure.testng;

import org.junit.Test;
import org.testng.ITestResult;
import org.testng.SkipException;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Kirill Kozlov kozlov.k.e@gmail.com
 *         Date: 04.02.14
 */
public class AllureTestResultAdaptorTest {

    @Test
    public void skipResultWithoutExceptionTest() {
        ITestResult iTestResult = mock(ITestResult.class);
        when(iTestResult.getStatus()).thenReturn(ITestResult.SKIP);
        when(iTestResult.getThrowable()).thenReturn(null);

        AllureTestResultAdaptor adaptor = new AllureTestResultAdaptor(iTestResult);

        assertThat(adaptor.getThrowable(), instanceOf(SkipException.class));
    }
}
