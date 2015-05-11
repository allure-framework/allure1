package ru.yandex.qatools.allure.testng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IConfigurationListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.TestException;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.events.AddParameterEvent;
import ru.yandex.qatools.allure.events.TestCaseCanceledEvent;
import ru.yandex.qatools.allure.events.TestCaseFailureEvent;
import ru.yandex.qatools.allure.events.TestCaseFinishedEvent;
import ru.yandex.qatools.allure.events.TestCasePendingEvent;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;
import ru.yandex.qatools.allure.events.TestSuiteFinishedEvent;
import ru.yandex.qatools.allure.events.TestSuiteStartedEvent;
import ru.yandex.qatools.allure.model.Description;
import ru.yandex.qatools.allure.model.ParameterKind;
import ru.yandex.qatools.allure.utils.AnnotationManager;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Allure framework listener for <a href="http://testng.org">TestNG</a> framework.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 22.11.13
 */
public class AllureTestListener implements ITestListener, IConfigurationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllureTestListener.class);

    private Allure lifecycle = Allure.LIFECYCLE;

    private Map<String, String> suiteUid = new HashMap<>();

    private Set<String> startedTestNames = Collections.newSetFromMap(
            new ConcurrentHashMap<String, Boolean>());

    @Override
    public void onConfigurationSuccess(ITestResult iTestResult) {
        //nothing
    }

    @Override
    public void onConfigurationFailure(ITestResult iTestResult) {
        if (!startedTestNames.contains(getName(iTestResult))) {
            onTestStart(iTestResult);
        }

        Throwable throwable = iTestResult.getThrowable();
        if (throwable == null) {
            throwable = new TestException("Test configuration failure");
        }

        getLifecycle().fire(new TestCaseCanceledEvent()
                        .withThrowable(throwable)
        );
        fireFinishTest();
    }

    @Override
    public void onConfigurationSkip(ITestResult iTestResult) {
        //nothing
    }

    @Override
    public void onStart(ITestContext iTestContext) {
        getLifecycle().fire(new TestSuiteStartedEvent(
                getSuiteUid(iTestContext), getCurrentSuiteTitle(iTestContext)
        ).withTitle(
                getCurrentSuiteTitle(iTestContext)
        ).withLabels(
                AllureModelUtils.createTestFrameworkLabel("TestNG")
        ));
        addPendingMethods(iTestContext);
    }


    private void addPendingMethods(ITestContext iTestContext) {
        for (ITestNGMethod method : iTestContext.getExcludedMethods()) {
            if (method.isTest() && !method.getEnabled()) {
                Description description = new Description().withValue(method.getDescription());
                TestCaseStartedEvent event = new TestCaseStartedEvent(getSuiteUid(iTestContext), method.getMethodName());
                if (description.getValue() != null) {
                    event.setDescription(description);
                }
                AnnotationManager am = new AnnotationManager(method.getConstructorOrMethod().getMethod().getAnnotations());
                am.setDefaults(method.getInstance().getClass().getAnnotations());
                am.update(event);
                getLifecycle().fire(event);
                getLifecycle().fire(new TestCasePendingEvent());
                fireFinishTest();
            }
        }
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        getLifecycle().fire(new TestSuiteFinishedEvent(getSuiteUid(iTestContext)));
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        ITestNGMethod method = iTestResult.getMethod();
        String suitePrefix = getCurrentSuitePrefix(iTestResult);
        String testName = getName(iTestResult);
        startedTestNames.add(testName);
        testName = testName.replace(suitePrefix, "");
        Description description = new Description().withValue(method.getDescription());
        TestCaseStartedEvent event = new TestCaseStartedEvent(getSuiteUid(iTestResult.getTestContext()), testName);
        if (description.getValue() != null) {
            event.setDescription(description);
        }
        AnnotationManager am = new AnnotationManager(getMethodAnnotations(iTestResult));
        am.setDefaults(getClassAnnotations(iTestResult));
        am.update(event);

        getLifecycle().fire(event);
        fireAddParameterEvents(iTestResult);
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        fireFinishTest();
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        getLifecycle().fire(new TestCaseFailureEvent()
                        .withThrowable(iTestResult.getThrowable())
        );
        fireFinishTest();
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        if (!startedTestNames.contains(getName(iTestResult))) {
            onTestStart(iTestResult);
        }

        Throwable throwable = iTestResult.getThrowable();
        if (throwable == null) {
            throwable = new SkipException("The test was skipped for some reason");
        }

        getLifecycle().fire(new TestCaseCanceledEvent()
                        .withThrowable(throwable)
        );
        fireFinishTest();
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        getLifecycle().fire(new TestCaseFailureEvent()
                        .withThrowable(iTestResult.getThrowable())
        );
        fireFinishTest();
    }

    public Annotation[] getMethodAnnotations(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getMethod().getAnnotations();
    }

    public Annotation[] getClassAnnotations(ITestResult iTestResult) {
        if (iTestResult.getInstance() == null) {
            return new Annotation[0];
        }
        return iTestResult.getInstance().getClass().getAnnotations();
    }

    private String getName(ITestResult iTestResult) {
        String suitePrefix = getCurrentSuitePrefix(iTestResult);
        StringBuilder sb = new StringBuilder(suitePrefix);
        sb.append(iTestResult.getName());
        Object[] parameters = iTestResult.getParameters();
        if (parameters != null && parameters.length > 0) {
            sb.append("[");
            for (Object parameter : parameters) {
                sb.append(parameter).append(",");
            }
            sb.replace(sb.length() - 1, sb.length(), "]");
        }
        return sb.toString();
    }

    String getCurrentSuiteTitle(ITestContext iTestContext) {
        String suite = iTestContext.getSuite().getName();
        String xmlTest = iTestContext.getCurrentXmlTest().getName();
        String params = "";

        if (!iTestContext.getCurrentXmlTest().getLocalParameters().isEmpty()) {
            params = iTestContext.getCurrentXmlTest().getLocalParameters()
                    .toString().replace("{", "[").replace("}", "]");
        }

        return suite + " : " + xmlTest + params;
    }

    private String getCurrentSuitePrefix(ITestResult iTestResult) {
        return "{" + getCurrentSuiteTitle(iTestResult.getTestContext()) + "}";
    }

    private void fireFinishTest() {
        getLifecycle().fire(new TestCaseFinishedEvent());
    }

    Allure getLifecycle() {
        return lifecycle;
    }

    void setLifecycle(Allure lifecycle) {
        this.lifecycle = lifecycle;
    }

    /**
     * Package private. Used in unit test.
     *
     * @return UID for the current suite
     */
    String getSuiteUid(ITestContext iTestContext) {
        String uid;
        String suite = getCurrentSuiteTitle(iTestContext);
        if (suiteUid.containsKey(suite)) {
            uid = suiteUid.get(suite);
        } else {
            uid = UUID.randomUUID().toString();
            suiteUid.put(suite, uid);
        }
        return uid;
    }

    /**
     * Creates test case parameters in XML in case of parametrized test (see TestNG @DataProvider).
     * Java 8 allows to keep test method parameter names in class files (needs javac -parameters compilation).
     * In Java 7 default parameter naming is used (arg0, arg1, ... ).
     *
     * @param iTestResult parameter passed to onTestStart() callback by TestNG
     * @see #getNameForParameter(String, List, int)
     * @see #getMethodParameterNamesIfAvailable(Method)
     */
    private void fireAddParameterEvents(ITestResult iTestResult) {
        Object[] parameters = iTestResult.getParameters();

        Method nativeMethod = iTestResult.getMethod().getConstructorOrMethod().getMethod();
        Annotation[][] parametersAnnotations = nativeMethod.getParameterAnnotations();

        List<String> methodParameterNames = getMethodParameterNamesIfAvailable(nativeMethod);

        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = getParameterAnnotation(parametersAnnotations[i]);
            if (parameter == null) {
                return;
            }

            String name = getNameForParameter(parameter.value(), methodParameterNames, i);
            String value = parameters[i].toString();

            getLifecycle().fire(new AddParameterEvent(name, value, ParameterKind.ARGUMENT));
        }
    }

    /**
     * Generate name for parameter.
     */
    private String getNameForParameter(String valueInAnnotation, List<String> methodParameterNames, int index) {
        if (!"".equals(valueInAnnotation)) {
            return valueInAnnotation;
        }

        if (index < methodParameterNames.size()) {
            return methodParameterNames.get(index);
        }

        return "arg" + index;
    }

    /**
     * Find {@link Parameter} annotation in given array of annotations.
     */
    private Parameter getParameterAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Parameter) {
                return (Parameter) annotation;
            }
        }
        return null;
    }

    /**
     * Returns true if can get method parameter names. Since java 8 you can get parameter names via
     * reflection if <code>-parameters</code> compiler argument specified. For more information
     * you can see documentation at
     * https://docs.oracle.com/javase/tutorial/reflect/member/methodparameterreflection.html .
     */
    private boolean canGetMethodParameterNames() { //NOSONAR
        try {
            Class.forName("java.lang.reflect.Parameter", false, getClass().getClassLoader());
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * Returns method parameter names if can.
     *
     * @see #canGetMethodParameterNames()
     */
    private List<String> getMethodParameterNamesIfAvailable(Method testMethod) {
        List<String> result = new ArrayList<>();

        if (!canGetMethodParameterNames()) {
            return result;
        }

        try {
            Object[] parameters = (Object[]) testMethod.getClass().getMethod("getParameters").invoke(testMethod);
            for (Object parameter : parameters) {
                result.add((String) parameter.getClass().getMethod("getName").invoke(parameter));
            }
            return result;
        } catch (Exception e) {
            String errorMessage = "Could not access parameter names via reflection. " +
                    "Falling back to default test method parameter names.";
            LOGGER.warn(errorMessage, e);
        }
        return result;
    }

}
