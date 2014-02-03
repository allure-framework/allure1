package ru.yandex.qatools.allure.testng;

import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.testng.annotations.*;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.events.*;

import java.lang.annotation.Annotation;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.mockito.Mockito.*;

/**
 * @author Kirill Kozlov kozlov.k.e@gmail.com
 *         Date: 02.02.14
 */
public class InternalAllureTestListenerTest {

    private InternalAllureTestListener internalListener;
    private Allure allure;

    @BeforeMethod
    public void setUp() {
        internalListener = new InternalAllureTestListener("suite");
        allure = mock(Allure.class);

        internalListener.setLifecycle(allure);
    }

    @Test
    public void skipTestFireTestCaseStartedEvent() {
        AllureTestResultAdaptor testResult = mock(AllureTestResultAdaptor.class);
        when(testResult.getName()).thenReturn("test");
        when(testResult.getAnnotations()).thenReturn(new Annotation[0]);

        internalListener.onTestSkipped(testResult);

        verify(allure).fire(isA(TestCaseStartedEvent.class));
    }

    @Test
    public void skipTestFiredSkippedEventWithThrowable() {
        AllureTestResultAdaptor testResult = mock(AllureTestResultAdaptor.class);
        when(testResult.getThrowable()).thenReturn(new NullPointerException());
        when(testResult.getName()).thenReturn("test");
        when(testResult.getAnnotations()).thenReturn(new Annotation[0]);

        internalListener.onTestSkipped(testResult);

        ArgumentCaptor<TestCaseEvent> firedEvent = ArgumentCaptor.forClass(TestCaseEvent.class);
        verify(allure).fire(firedEvent.capture());

        assertThat(firedEvent.getValue(), instanceOf(TestCaseSkippedEvent.class));
        TestCaseSkippedEvent skippedEvent = (TestCaseSkippedEvent)firedEvent.getValue();
        assertThat(skippedEvent.getThrowable(), instanceOf(NullPointerException.class));
    }

    @Test
    public void skipTestFiredEventsOrder() {
        AllureTestResultAdaptor testResult = mock(AllureTestResultAdaptor.class);
        when(testResult.getThrowable()).thenReturn(new NullPointerException());
        when(testResult.getName()).thenReturn("test");
        when(testResult.getAnnotations()).thenReturn(new Annotation[0]);

        internalListener.onTestSkipped(testResult);

        InOrder inOrder = inOrder(allure);
        inOrder.verify(allure).fire(isA(TestCaseStartedEvent.class));
        inOrder.verify(allure).fire(isA(TestCaseSkippedEvent.class));
        inOrder.verify(allure).fire(isA(TestCaseFinishedEvent.class));
    }
}
