package ru.yandex.qatools.allure.converters;

import ru.yandex.qatools.allure.AllureTestCase;
import ru.yandex.qatools.allure.model.TestSuiteResult;

import java.util.List;

/**
 * eroshenkoam
 * 02/02/15
 */
public interface TestSuiteConverter {

    List<AllureTestCase> convert(TestSuiteResult testSuiteResult);
}
