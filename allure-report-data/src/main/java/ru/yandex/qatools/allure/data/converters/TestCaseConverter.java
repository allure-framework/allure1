package ru.yandex.qatools.allure.data.converters;

import ru.yandex.qatools.allure.data.AllureTestCase;
import ru.yandex.qatools.allure.model.TestCaseResult;

/**
 * eroshenkoam
 * 02/02/15
 */
public interface TestCaseConverter {

    AllureTestCase convert(TestCaseResult testCaseResult);

}
