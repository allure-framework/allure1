package ru.yandex.qatools.allure.io;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 08.10.15
 */
public class TestSuiteReader implements Reader<TestSuiteResult> {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestSuiteReader.class);

    private final Path[] resultDirectories;

    @Inject
    public TestSuiteReader(@ResultDirectories Path... resultDirectories) {
        this.resultDirectories = resultDirectories;
    }

    @Override
    public Iterator<TestSuiteResult> iterator() {
        try {
            return Iterators.concat(
                    new JsonTestSuiteResultIterator(resultDirectories),
                    new XmlTestSuiteResultIterator(resultDirectories)
            );
        } catch (IOException e) {
            LOGGER.error("Could not create iterator", e);
            return ImmutableSet.<TestSuiteResult>of().iterator();
        }
    }
}
