package ru.yandex.qatools.allure.io;

import ru.yandex.qatools.allure.model.Label;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import static ru.yandex.qatools.allure.io.TestCaseReader.SUITE_NAME;
import static ru.yandex.qatools.allure.io.TestCaseReader.SUITE_TITLE;
import static ru.yandex.qatools.allure.utils.DescriptionUtils.mergeDescriptions;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 08.10.15
 */
public class TestCaseResultIterator implements Iterator<TestCaseResult> {

    private final TestSuiteResult testSuite;

    private final Iterator<TestCaseResult> iterator;

    private final Label suiteNameLabel;

    private final Label suiteTitleLabel;

    public TestCaseResultIterator(TestSuiteResult testSuite) {
        this.testSuite = testSuite;
        this.iterator = testSuite.getTestCases().iterator();
        this.suiteNameLabel = new Label().withName(SUITE_NAME).withValue(testSuite.getName());
        this.suiteTitleLabel = new Label().withName(SUITE_TITLE).withValue(testSuite.getTitle());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestCaseResult next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        TestCaseResult result = iterator.next();
        Set<Label> labels = new HashSet<>(testSuite.getLabels());
        labels.add(suiteNameLabel);
        labels.add(suiteTitleLabel);
        labels.addAll(result.getLabels());
        result.setLabels(new ArrayList<>(labels));
        result.setDescription(mergeDescriptions(testSuite, result));

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
