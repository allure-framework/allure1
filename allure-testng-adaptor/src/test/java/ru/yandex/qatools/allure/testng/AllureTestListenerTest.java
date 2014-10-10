package ru.yandex.qatools.allure.testng;

import org.junit.*;
import org.mockito.InOrder;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.xml.XmlTest;

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
    private static final String DEFAULT_SUITE_NAME = "suite";
    private static final String DEFAULT_XML_TEST_NAME = "testxml";
    
    private AllureTestListener testngListener;
    private Allure allure;
    private ITestContext testContext;
    
    @Before
    public void setUp() {
        testngListener = spy(new AllureTestListener());
        allure = mock(Allure.class);

        testngListener.setLifecycle(allure);
        
        ISuite suite = mock(ISuite.class);
    	when(suite.getName()).thenReturn(DEFAULT_SUITE_NAME);
    	XmlTest xmlTest = mock(XmlTest.class);
    	when(xmlTest.getName()).thenReturn(DEFAULT_XML_TEST_NAME);
    	testContext = mock(ITestContext.class);
    	when(testContext.getSuite()).thenReturn(suite);
    	when(testContext.getCurrentXmlTest()).thenReturn(xmlTest);
    }

    @Test
    public void skipTestFireTestCaseStartedEvent() {
        ITestResult testResult = mock(ITestResult.class);
        when(testResult.getName()).thenReturn(DEFAULT_TEST_NAME);
        when(testResult.getTestContext()).thenReturn(testContext);
        doReturn(new Annotation[0]).when(testngListener).getMethodAnnotations(testResult);
        ITestNGMethod method = mock(ITestNGMethod.class);
        when(method.getDescription()).thenReturn(null);
        when(testResult.getMethod()).thenReturn(method);

        testngListener.onTestSkipped(testResult);

        String suiteUid = testngListener.getSuiteUid(testContext);
        verify(allure).fire(eq(new TestCaseStartedEvent(suiteUid, DEFAULT_TEST_NAME)));
    }

    @Test
    public void skipTestWithThrowable() {
        ITestResult testResult = mock(ITestResult.class);
        Throwable throwable = new NullPointerException();
        when(testResult.getTestContext()).thenReturn(testContext);
        when(testResult.getThrowable()).thenReturn(throwable);
        when(testResult.getName()).thenReturn(DEFAULT_TEST_NAME);
        ITestNGMethod method = mock(ITestNGMethod.class);
        when(method.getDescription()).thenReturn(null);
        when(testResult.getMethod()).thenReturn(method);
        
        doReturn(new Annotation[0]).when(testngListener).getMethodAnnotations(testResult);

        testngListener.onTestSkipped(testResult);

        verify(allure).fire(eq(new TestCaseCanceledEvent().withThrowable(throwable)));
    }

    @Test
    public void skipTestWithoutThrowable() {
        ITestResult testResult = mock(ITestResult.class);
        when(testResult.getTestContext()).thenReturn(testContext);
        when(testResult.getName()).thenReturn(DEFAULT_TEST_NAME);
        ITestNGMethod method = mock(ITestNGMethod.class);
        when(method.getDescription()).thenReturn(null);
        when(testResult.getMethod()).thenReturn(method);
        
        doReturn(new Annotation[0]).when(testngListener).getMethodAnnotations(testResult);

        testngListener.onTestSkipped(testResult);

        verify(allure).fire(isA(TestCaseCanceledEvent.class));
    }

    @Test
    public void skipTestFiredEventsOrder() {
        ITestResult testResult = mock(ITestResult.class);
        when(testResult.getTestContext()).thenReturn(testContext);
        when(testResult.getThrowable()).thenReturn(new NullPointerException());
        when(testResult.getName()).thenReturn(DEFAULT_TEST_NAME);
        ITestNGMethod method = mock(ITestNGMethod.class);
        when(method.getDescription()).thenReturn(null);
        when(testResult.getMethod()).thenReturn(method);
        
        doReturn(new Annotation[0]).when(testngListener).getMethodAnnotations(testResult);

        testngListener.onTestSkipped(testResult);

        InOrder inOrder = inOrder(allure);
        inOrder.verify(allure).fire(isA(TestCaseStartedEvent.class));
        inOrder.verify(allure).fire(isA(TestCaseCanceledEvent.class));
        inOrder.verify(allure).fire(isA(TestCaseFinishedEvent.class));
    }

    @Test
    public void parametrizedTest() {
        double doubleParameter = 10.0;
        String stringParameter = "string";
        ITestResult testResult = mock(ITestResult.class);
        when(testResult.getTestContext()).thenReturn(testContext);
        when(testResult.getName()).thenReturn(DEFAULT_TEST_NAME);
        when(testResult.getParameters()).thenReturn(new Object[] { doubleParameter, stringParameter});
        ITestNGMethod method = mock(ITestNGMethod.class);
        when(method.getDescription()).thenReturn(null);
        when(testResult.getMethod()).thenReturn(method);
        
        doReturn(new Annotation[0]).when(testngListener).getMethodAnnotations(testResult);

        testngListener.onTestStart(testResult);

        String suiteUid = testngListener.getSuiteUid(testContext);
        String testName = String.format("%s[%s,%s]",
                DEFAULT_TEST_NAME, Double.toString(doubleParameter), stringParameter);
        verify(allure).fire(eq(new TestCaseStartedEvent(suiteUid, testName)));
    }
}
