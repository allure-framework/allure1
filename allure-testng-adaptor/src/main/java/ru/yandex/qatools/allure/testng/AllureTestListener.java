package ru.yandex.qatools.allure.testng;

import org.testng.IConfigurationListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.SkipException;
import org.testng.TestException;

import ru.yandex.qatools.allure.Allure;
import ru.yandex.qatools.allure.config.AllureModelUtils;
import ru.yandex.qatools.allure.events.TestCaseCanceledEvent;
import ru.yandex.qatools.allure.events.TestCaseFailureEvent;
import ru.yandex.qatools.allure.events.TestCaseFinishedEvent;
import ru.yandex.qatools.allure.events.TestCaseStartedEvent;
import ru.yandex.qatools.allure.events.TestSuiteFinishedEvent;
import ru.yandex.qatools.allure.events.TestSuiteStartedEvent;
import ru.yandex.qatools.allure.utils.AnnotationManager;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashMap;
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

    private Allure lifecycle = Allure.LIFECYCLE;

    private Map<String,String> suiteUid = new HashMap<String, String>(); 

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
                getSuiteUid(iTestContext), iTestContext.getSuite().getName()
        ).withLabels(
                AllureModelUtils.createTestFrameworkLabel("TestNG"),
                AllureModelUtils.createFeatureLabel(iTestContext.getName())
        ));
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        getLifecycle().fire(new TestSuiteFinishedEvent(getSuiteUid(iTestContext)));
    }

    @Override
    public void onTestStart(ITestResult iTestResult) {
    	String suitePrefix=getCurrentSuitePrefix(iTestResult);
        String testName = getName(iTestResult).replace(suitePrefix,"");
        startedTestNames.add(testName);

        TestCaseStartedEvent event = new TestCaseStartedEvent(getSuiteUid(iTestResult.getTestContext()), testName);
        AnnotationManager am = new AnnotationManager(getMethodAnnotations(iTestResult));

        am.update(event);

        getLifecycle().fire(event);
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

    private String getName(ITestResult iTestResult) {
    	String suitePrefix = getCurrentSuitePrefix(iTestResult);
        StringBuilder sb = new StringBuilder(suitePrefix + iTestResult.getName());
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
    
    private String getCurrentSuitePrefix(ITestResult iTestResult){
    	return "{" +iTestResult.getTestContext().getSuite().getName() +"}";
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

    String getSuiteUid(ITestContext iTestContext) {
    	String uid;
    	String suite=iTestContext.getSuite().getName();
    	if (suiteUid.containsKey(suite)){
    		uid=suiteUid.get(suite);
    	} else {
    		uid=UUID.randomUUID().toString();
    		suiteUid.put(suite, uid);
    	}
        return uid;
    }
}
