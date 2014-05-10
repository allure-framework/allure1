package ru.yandex.qatools.allure.storages;

import org.junit.Test;
import ru.yandex.qatools.allure.model.TestCaseResult;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.12.13
 */
public class TestCaseStorageTest {

    @Test
    public void simpleTest() throws Exception {
        TestCaseStorage storage = new TestCaseStorage();
        TestCaseResult testCase = storage.get();
        assertTrue(testCase == storage.get());
    }
}
