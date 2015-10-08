package ru.yandex.qatools.allure.io;

import com.google.inject.Inject;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.Iterator;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 09.02.15
 */
public class TestCaseReader implements Reader<TestCaseResult> {

    public static final String SUITE_NAME = "suite-name";
    public static final String SUITE_TITLE = "suite-title";

    private final Reader<TestSuiteResult> testSuites;

    @Inject
    public TestCaseReader(Reader<TestSuiteResult> suiteResultsReader) {
        testSuites = suiteResultsReader;
    }

    @Override
    public Iterator<TestCaseResult> iterator() {
        return new TestCaseResultIterator2(testSuites);
    }
}
