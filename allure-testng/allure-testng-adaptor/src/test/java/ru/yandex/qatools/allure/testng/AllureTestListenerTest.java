package ru.yandex.qatools.allure.testng;

import org.junit.*;
import org.mockito.InOrder;
import org.testng.ITestResult;
import org.testng.SkipException;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.*;

import java.lang.annotation.Annotation;

import static org.mockito.Mockito.*;

/**
 * @author Kirill Kozlov kozlov.k.e@gmail.com
 *         Date: 02.02.14
 */
public class AllureTestListenerTest {

    private static final String DEFAULT_TEST_NAME = "test";

    private AllureTestListener testngListener;
    private Allure allure;

    @Before
    public void setUp() {
        testngListener = spy(new AllureTestListener());
        allure = mock(Allure.class);

        testngListener.setLifecycle(allure);
    }

    @Test
    public void skipTestFireTestCaseStartedEvent() {
        ITestResult testResult = mock(ITestResult.class);
        when(testResult.getName()).thenReturn(DEFAULT_TEST_NAME);

        doReturn(new Annotation[0]).when(testngListener).getMethodAnnotations(testResult);

        testngListener.onTestSkipped(testResult);

        String suiteUid = testngListener.getSuiteUid();
        verify(allure).fire(eq(new TestCaseStartedEvent(suiteUid, DEFAULT_TEST_NAME)));
    }

    @Test
    public void skipTestWithThrowable() {
        ITestResult testResult = mock(ITestResult.class);
        Throwable throwable = new NullPointerException();
        when(testResult.getThrowable()).thenReturn(throwable);
        when(testResult.getName()).thenReturn(DEFAULT_TEST_NAME);

        doReturn(new Annotation[0]).when(testngListener).getMethodAnnotations(testResult);

        testngListener.onTestSkipped(testResult);

        verify(allure).fire(eq(new TestCaseSkippedEvent().withThrowable(throwable)));
    }

    @Test
    public void skipTestWithoutThrowable() {
        ITestResult testResult = mock(ITestResult.class);
        when(testResult.getName()).thenReturn(DEFAULT_TEST_NAME);

        doReturn(new Annotation[0]).when(testngListener).getMethodAnnotations(testResult);

        testngListener.onTestSkipped(testResult);

        verify(allure).fire(isA(TestCaseSkippedEvent.class));
    }

    @Test
    public void skipTestFiredEventsOrder() {
        ITestResult testResult = mock(ITestResult.class);
        when(testResult.getThrowable()).thenReturn(new NullPointerException());
        when(testResult.getName()).thenReturn(DEFAULT_TEST_NAME);

        doReturn(new Annotation[0]).when(testngListener).getMethodAnnotations(testResult);

        testngListener.onTestSkipped(testResult);

        InOrder inOrder = inOrder(allure);
        inOrder.verify(allure).fire(isA(TestCaseStartedEvent.class));
        inOrder.verify(allure).fire(isA(TestCaseSkippedEvent.class));
        inOrder.verify(allure).fire(isA(TestCaseFinishedEvent.class));
    }
}
