package ru.yandex.qatools.allure.data.io;

import com.google.inject.Inject;
import ru.yandex.qatools.allure.model.Label;
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

    private Iterator<TestSuiteResult> testSuites;

    private Iterator<TestCaseResult> testCases;

    private TestSuiteResult currentSuite;

    @Inject
    public TestCaseReader(Reader<TestSuiteResult> suiteResultsReader) {
        testSuites = suiteResultsReader.iterator();
    }

    @Override
    public Iterator<TestCaseResult> iterator() {
        return new Iterator<TestCaseResult>() {

            private boolean nextSuite() {
                if ((testCases == null || !testCases.hasNext()) && testSuites.hasNext()) {

                    currentSuite = testSuites.next();
                    testCases = currentSuite.getTestCases().iterator();

                    return true;
                }
                return false;
            }

            @Override
            public boolean hasNext() {
                return testCases != null && testCases.hasNext() || nextSuite() && hasNext();
            }

            @Override
            public TestCaseResult next() {
                if (!hasNext()) {
                    return null;
                }

                TestCaseResult result = testCases.next();

                result.getLabels().add(new Label().withName(SUITE_NAME).withValue(currentSuite.getName()));
                result.getLabels().add(new Label().withName(SUITE_TITLE).withValue(currentSuite.getTitle()));
                result.getLabels().addAll(currentSuite.getLabels());

                return result;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

        };
    }

}
