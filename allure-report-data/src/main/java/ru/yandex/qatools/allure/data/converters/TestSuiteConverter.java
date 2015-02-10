package ru.yandex.qatools.allure.data.converters;

import ru.yandex.qatools.allure.data.AllureTestCase;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.List;

/**
 * eroshenkoam
 * 02/02/15
 */
public interface TestSuiteConverter {

    List<AllureTestCase> convert(TestSuiteResult testSuiteResult);
}
