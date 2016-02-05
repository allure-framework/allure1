package ru.yandex.qatools.allure.data.io;

import com.google.inject.Inject;
import ru.yandex.qatools.allure.model.Description;
import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static ru.yandex.qatools.allure.data.utils.DescriptionUtils.mergeDescriptions;

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
        return new TestCaseResultIterator();
    }

    private class TestCaseResultIterator implements Iterator<TestCaseResult> {

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

            Set<Label> labels = new HashSet<>(currentSuite.getLabels());
            labels.add(new Label().withName(SUITE_NAME).withValue(currentSuite.getName()));
            labels.add(new Label().withName(SUITE_TITLE).withValue(currentSuite.getTitle()));
            labels.addAll(result.getLabels());

            result.setLabels(new ArrayList<>(labels));

            Description description = mergeDescriptions(currentSuite, result);
            result.setDescription(description);

            return result;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
}
