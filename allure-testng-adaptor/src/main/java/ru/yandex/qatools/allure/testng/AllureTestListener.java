package ru.yandex.qatools.allure.testng;

import static java.util.Arrays.asList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;
import org.testng.internal.IResultListener;
import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.config.AllureConfig;
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
import java.util.Objects;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Allure framework listener for <a href="http://testng.org">TestNG</a> framework.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 22.11.13
 */
public class AllureTestListener implements IResultListener, ISuiteListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(AllureTestListener.class);
    private static final String SUITE_UID = "SUITE_UID";
    private Allure lifecycle = Allure.LIFECYCLE;
    private Set<String> startedTestNames = Collections.newSetFromMap(
            new ConcurrentHashMap<String, Boolean>());

    @Override
    public void onStart(ISuite suite) {
        //For future development
        //Currently Allure model is not supporting TestNG Suite. 
        //In Allure terminology Suite is a combination of TestNg Suite XML tag and TestNg Test XML tag
    }

    @Override
    public void onFinish(ISuite suite) {
        //For future development
        //Currently Allure model is not supporting TestNG Suite. 
        //In Allure terminology Suite is a combination of TestNg Suite XML tag and TestNg Test XML tag
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

    @Override
    public void onFinish(ITestContext iTestContext) {
        getLifecycle().fire(new TestSuiteFinishedEvent(getSuiteUid(iTestContext)));
    }

    @Override
    public void onConfigurationSuccess(ITestResult iTestResult) {
        //Configuration method will be shown in the report on failure or on skip only
    }

    @Override
    public void onConfigurationFailure(ITestResult iTestResult) {
        if (isSuppressConfigEvent(iTestResult)) {
            return;
        }
        String suiteUid = getSuiteUid(iTestResult.getTestContext());
        if (isAfterSuiteConfigMethod(iTestResult)) {
            String suiteTitle = getCurrentSuiteTitle(iTestResult.getTestContext());
            getLifecycle().fire(new TestSuiteStartedEvent(suiteUid, suiteTitle).withTitle(suiteTitle));
        }
        Throwable throwable = iTestResult.getThrowable();
        createConfigEvent(iTestResult);
        getLifecycle().fire(new TestCaseFailureEvent().withThrowable(throwable));
        fireFinishTest();
        if (isAfterSuiteConfigMethod(iTestResult)) {
            getLifecycle().fire(new TestSuiteFinishedEvent(suiteUid));
        }
    }

    @Override
    public void onConfigurationSkip(ITestResult iTestResult) {
        if (isSuppressConfigEvent(iTestResult)) {
            return;
        }
        String suiteUid = getSuiteUid(iTestResult.getTestContext());
        if (isAfterSuiteConfigMethod(iTestResult)) {
            String suiteTitle = getCurrentSuiteTitle(iTestResult.getTestContext());
            getLifecycle().fire(new TestSuiteStartedEvent(suiteUid, suiteTitle).withTitle(suiteTitle));
        }
        createConfigEvent(iTestResult);
        fireTestCaseCancel(iTestResult);
        fireFinishTest();
        if (isAfterSuiteConfigMethod(iTestResult)) {
            getLifecycle().fire(new TestSuiteFinishedEvent(suiteUid));
        }
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
        ITestNGMethod method = iTestResult.getMethod();
        String testSuiteLabel = iTestResult.getTestContext().getSuite().getName();
        String testGroupLabel = iTestResult.getTestContext().getCurrentXmlTest().getName();
        String testClassLabel = iTestResult.getTestClass().getName();
        String testMethodLabel = method.getMethodName();
        String suitePrefix = getCurrentSuitePrefix(iTestResult);
        String testName = getName(iTestResult);
        startedTestNames.add(testName);
        testName = testName.replace(suitePrefix, "");

        String invoc = getMethodInvocationsAndSuccessPercentage(iTestResult);
        Description description = new Description().withValue(method.getDescription());
        String suiteUid = getSuiteUid(iTestResult.getTestContext());
        TestCaseStartedEvent event = new TestCaseStartedEvent(suiteUid, testName + invoc).withLabels(
                AllureModelUtils.createTestSuiteLabel(testSuiteLabel),
                AllureModelUtils.createTestGroupLabel(testGroupLabel),
                AllureModelUtils.createTestClassLabel(testClassLabel),
                AllureModelUtils.createTestMethodLabel(testMethodLabel));
        if (description.getValue() != null) {
            event.setDescription(description);
        }
        AnnotationManager am = new AnnotationManager(getMethodAnnotations(iTestResult));
        am.setDefaults(getClassAnnotations(iTestResult));
        am.update(event);

        getLifecycle().fire(event);

        if (AllureConfig.newInstance().areTestNgParametersEnabled()) {
            fireAddParameterEvents(iTestResult);
        }
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
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        Throwable throwable = iTestResult.getThrowable();
        getLifecycle().fire(new TestCaseCanceledEvent().withThrowable(throwable));
        fireFinishTest();
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        if (!startedTestNames.contains(getName(iTestResult))) {
            onTestStart(iTestResult);
        }
        fireTestCaseCancel(iTestResult);
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
        if (iTestContext.getAttribute(SUITE_UID) != null) {
            uid = (String) iTestContext.getAttribute(SUITE_UID);
        } else {
            uid = UUID.randomUUID().toString();
            iTestContext.setAttribute(SUITE_UID, uid);
        }
        return uid;
    }

    String getCurrentSuiteTitle(ITestContext iTestContext) {
        String suite = iTestContext.getSuite().getName();
        String xmlTest = iTestContext.getCurrentXmlTest().getName();
        String params = "";

        if (!iTestContext.getCurrentXmlTest().getLocalParameters().isEmpty() &&
                AllureConfig.newInstance().areTestNgParametersEnabled()) {
            params = iTestContext.getCurrentXmlTest().getLocalParameters()
                    .toString().replace("{", "[").replace("}", "]");
        }

        return suite + " : " + xmlTest + params;
    }

    private String getName(ITestResult iTestResult) {
        String suitePrefix = getCurrentSuitePrefix(iTestResult);
        StringBuilder sb = new StringBuilder(suitePrefix);
        sb.append(iTestResult.getName());

        if (AllureConfig.newInstance().areTestNgParametersEnabled()) {
            Object[] parameters = iTestResult.getParameters();
            if (parameters != null && parameters.length > 0) {
                sb.append("[");
                for (Object parameter : parameters) {
                    sb.append(parameter).append(",");
                }
                sb.replace(sb.length() - 1, sb.length(), "]");
            }
        }

        return sb.toString();
    }

    private String getCurrentSuitePrefix(ITestResult iTestResult) {
        return "{" + getCurrentSuiteTitle(iTestResult.getTestContext()) + "}";
    }

    private void addPendingMethods(ITestContext iTestContext) {
        for (ITestNGMethod method : iTestContext.getExcludedMethods()) {
            if (method.isTest() && !method.getEnabled() && isInActiveGroup(method, iTestContext)) {
                Description description = new Description().withValue(method.getDescription());
                String suiteUid = getSuiteUid(iTestContext);
                TestCaseStartedEvent event = new TestCaseStartedEvent(suiteUid, method.getMethodName());
                if (description.getValue() != null) {
                    event.setDescription(description);
                }
                Annotation[] annotations = method.getConstructorOrMethod().getMethod().getAnnotations();
                AnnotationManager am = new AnnotationManager(annotations);
                am.setDefaults(method.getInstance().getClass().getAnnotations());
                am.update(event);
                getLifecycle().fire(event);
                getLifecycle().fire(new TestCasePendingEvent());
                fireFinishTest();
            }
        }
    }

    /**
     * Checks if the given test method is part of the current test run in matters of the include/exclude group filtering 
     */
    private boolean isInActiveGroup(ITestNGMethod method, ITestContext context) {
        String[] includedGroupsArray = context.getIncludedGroups();
        List<String> includeGroups = includedGroupsArray != null ? asList(includedGroupsArray) : Collections.<String>emptyList();
        String[] excludedGroupsArray = context.getExcludedGroups();
        List<String> excludeGroups = excludedGroupsArray != null ? asList(excludedGroupsArray) : Collections.<String>emptyList();
        if (includeGroups.isEmpty()) {
            if (excludeGroups.isEmpty()) {
                return true; // no group restriction
            } else {
                return isInActiveGroupWithoutIncludes(method, excludeGroups);
            }
        } else {
            return isInActiveGroupWithIncludes(method, includeGroups, excludeGroups);
        }
    }

    private boolean isInActiveGroupWithoutIncludes(ITestNGMethod method, List<String> excludeGroups) {
        if (method.getGroups() != null) {
            for (String group : method.getGroups()) {
                if (excludeGroups.contains(group)) {
                    return false; // a group of the method is excluded
                }
            }
        }
        return true; // no groups of the method are excluded
    }

    private boolean isInActiveGroupWithIncludes(ITestNGMethod method, List<String> includeGroups, List<String> excludeGroups) {
        if (method.getGroups() != null) {
            boolean included = false;
            boolean excluded = false;
            for (String group : method.getGroups()) {
                if (includeGroups.contains(group)) {
                    included = true;
                }
                if (excludeGroups.contains(group)) {
                    excluded = true;
                }
            }
            if (included && !excluded) {
                return true; // a group of the method is included and not excluded
            }
        }
        return false; // no groups of the method are included
    }

    private String getMethodInvocationsAndSuccessPercentage(ITestResult iTestResult) {
        int percentage = iTestResult.getMethod().getSuccessPercentage();
        int curCount = iTestResult.getMethod().getCurrentInvocationCount() + 1;
        int iCount = iTestResult.getMethod().getInvocationCount();
        String invoc = "";
        if (iCount > 1) {
            invoc = ":" + curCount + "/" + iCount;
            if (percentage > 0) {
                invoc = invoc + " " + percentage + "%";
            }
        }
        return invoc;
    }

    private ConfigMethodType getConfigMethodType(ITestResult iTestResult) {
        if (iTestResult.getMethod().isBeforeSuiteConfiguration()) {
            return ConfigMethodType.BEFORE_SUITE;
        }
        if (iTestResult.getMethod().isBeforeTestConfiguration()) {
            return ConfigMethodType.BEFORE_TEST;
        }
        if (iTestResult.getMethod().isBeforeClassConfiguration()) {
            return ConfigMethodType.BEFORE_CLASS;
        }
        if (iTestResult.getMethod().isBeforeGroupsConfiguration()) {
            return ConfigMethodType.BEFORE_GROUPS;
        }
        if (iTestResult.getMethod().isBeforeMethodConfiguration()) {
            return ConfigMethodType.BEFORE_METHOD;
        }
        if (iTestResult.getMethod().isAfterSuiteConfiguration()) {
            return ConfigMethodType.AFTER_SUITE;
        }
        if (iTestResult.getMethod().isAfterTestConfiguration()) {
            return ConfigMethodType.AFTER_TEST;
        }
        if (iTestResult.getMethod().isAfterClassConfiguration()) {
            return ConfigMethodType.AFTER_CLASS;
        }
        if (iTestResult.getMethod().isAfterGroupsConfiguration()) {
            return ConfigMethodType.AFTER_GROUPS;
        }
        if (iTestResult.getMethod().isAfterMethodConfiguration()) {
            return ConfigMethodType.AFTER_METHOD;
        }
        return null;
    }

    private boolean isAfterSuiteConfigMethod(ITestResult iTestResult) {
        return ConfigMethodType.AFTER_SUITE.equals(getConfigMethodType(iTestResult));
    }

    /**
     * Suppress duplicated configuration method events
     */
    @SuppressWarnings("unchecked")
    private synchronized boolean isSuppressConfigEvent(ITestResult iTestResult) {
        Set<String> methodNames;
        ITestContext context = iTestResult.getTestContext();
        String configType = getConfigMethodType(iTestResult).getName();
        if (context.getAttribute(configType) == null) {
            methodNames = new HashSet<>();
            methodNames.add(iTestResult.getName());
            context.setAttribute(configType, methodNames);
            return false;
        } else {
            methodNames = (Set<String>) context.getAttribute(configType);
            if (!methodNames.contains(iTestResult.getName())) {
                methodNames.add(iTestResult.getName());
                return false;
            }
        }
        return true;
    }

    private void createConfigEvent(ITestResult iTestResult) {
        String description = iTestResult.getMethod().getDescription();
        if (description == null || description.isEmpty()) {
            description = getConfigMethodType(iTestResult).getName();
        }
        String suiteUid = getSuiteUid(iTestResult.getTestContext());
        TestCaseStartedEvent event = new TestCaseStartedEvent(suiteUid, iTestResult.getName());
        event.setDescription(new Description().withValue(description));
        AnnotationManager am = new AnnotationManager(getMethodAnnotations(iTestResult));
        am.setDefaults(getClassAnnotations(iTestResult));
        am.update(event);
        getLifecycle().fire(event);
    }

    private void fireFinishTest() {
        getLifecycle().fire(new TestCaseFinishedEvent());
    }

    private void fireTestCaseCancel(ITestResult iTestResult) {
        Throwable skipMessage = new Throwable() {
            private static final long serialVersionUID = 1L;

            @Override
            public String getMessage() {
                return "Skipped due to dependency on other skipped or failed test methods";
            }
        };
        getLifecycle().fire(new TestCaseCanceledEvent().withThrowable(skipMessage));
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
                continue;
            }

            String name = getNameForParameter(parameter.value(), methodParameterNames, i);
            String value = Objects.toString(parameters[i]);

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
    private boolean canGetMethodParameterNames() {
        try {
            Class.forName("java.lang.reflect.Parameter", false, Thread.currentThread().getContextClassLoader());
            return true;
        } catch (ClassNotFoundException e) { //NOSONAR
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

    enum ConfigMethodType {
        BEFORE_SUITE("BeforeSuite"), BEFORE_TEST("BeforeTest"), BEFORE_CLASS("BeforeClass"),
        BEFORE_GROUPS("BeforeGroups"), BEFORE_METHOD("BeforeMethod"), AFTER_SUITE("AfterSuite"),
        AFTER_TEST("AfterTest"), AFTER_CLASS("AfterClass"), AFTER_GROUPS("AfterGroups"),
        AFTER_METHOD("AfterMethod");

        private String title;

        ConfigMethodType(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public String getName() {
            return "@" + this.getTitle();
        }
    }
}
