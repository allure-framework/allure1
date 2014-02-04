package ru.yandex.qatools.allure.testng;

import org.mockito.InOrder;
import org.testng.annotations.*;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.*;

import java.lang.annotation.Annotation;

import static org.mockito.Mockito.*;

/**
 * @author Kirill Kozlov kozlov.k.e@gmail.com
 *         Date: 02.02.14
 */
public class InternalAllureTestListenerTest {

    private static final String DEFAULT_SUITE_NAME = "suite";
    private static final String DEFAULT_TEST_NAME = "test";

    private InternalAllureTestListener internalListener;
    private Allure allure;

    @BeforeMethod
    public void setUp() {
        internalListener = new InternalAllureTestListener(DEFAULT_SUITE_NAME);
        allure = mock(Allure.class);

        internalListener.setLifecycle(allure);
    }

    @Test
    public void skipTestFireTestCaseStartedEvent() {
        AllureTestResultAdaptor testResult = mock(AllureTestResultAdaptor.class);
        when(testResult.getName()).thenReturn(DEFAULT_TEST_NAME);
        when(testResult.getAnnotations()).thenReturn(new Annotation[0]);

        internalListener.onTestSkipped(testResult);

        verify(allure).fire(eq(new TestCaseStartedEvent(DEFAULT_SUITE_NAME, DEFAULT_TEST_NAME)));
    }

    @Test
    public void skipTestFiredSkippedEventWithThrowable() {
        AllureTestResultAdaptor testResult = mock(AllureTestResultAdaptor.class);
        Throwable throwable = new NullPointerException();
        when(testResult.getThrowable()).thenReturn(throwable);
        when(testResult.getName()).thenReturn("test");
        when(testResult.getAnnotations()).thenReturn(new Annotation[0]);

        internalListener.onTestSkipped(testResult);

        verify(allure).fire(eq(new TestCaseSkippedEvent().withThrowable(throwable)));
    }

    @Test
    public void skipTestFiredEventsOrder() {
        AllureTestResultAdaptor testResult = mock(AllureTestResultAdaptor.class);
        when(testResult.getThrowable()).thenReturn(new NullPointerException());
        when(testResult.getName()).thenReturn(DEFAULT_TEST_NAME);
        when(testResult.getAnnotations()).thenReturn(new Annotation[0]);

        internalListener.onTestSkipped(testResult);

        InOrder inOrder = inOrder(allure);
        inOrder.verify(allure).fire(isA(TestCaseStartedEvent.class));
        inOrder.verify(allure).fire(isA(TestCaseSkippedEvent.class));
        inOrder.verify(allure).fire(isA(TestCaseFinishedEvent.class));
    }
}
