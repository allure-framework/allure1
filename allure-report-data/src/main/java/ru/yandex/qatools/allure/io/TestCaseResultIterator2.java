package ru.yandex.qatools.allure.io;

import com.google.common.collect.ImmutableSet;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 08.10.15
 */
public class TestCaseResultIterator2 implements Iterator<TestCaseResult> {

    private final Iterator<TestSuiteResult> testSuites;

    private Iterator<TestCaseResult> current = ImmutableSet.<TestCaseResult>of().iterator();

    public TestCaseResultIterator2(Reader<TestSuiteResult> testSuites) {
        this.testSuites = testSuites.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        if (current.hasNext()) {
            return true;
        }

        if (!testSuites.hasNext()) {
            return false;
        }

        current = new TestCaseResultIterator(testSuites.next());
        return hasNext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TestCaseResult next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        return current.next();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
