package ru.yandex.qatools.allure.data.utils;

import org.jvnet.jaxb2_commons.lang.StringUtils;
import ru.yandex.qatools.allure.model.Description;
import ru.yandex.qatools.allure.model.DescriptionType;
import ru.yandex.qatools.allure.model.TestCaseResult;
import ru.yandex.qatools.allure.model.TestSuiteResult;

/**
 * eroshenkoam
 * 11/03/15
 */
public class DescriptionUtils {

    public static Description mergeDescriptions(TestSuiteResult testSuiteResult, TestCaseResult testCaseResult) {
        return mergeDescriptions(testSuiteResult.getDescription(), testCaseResult.getDescription());
    }

    public static Description mergeDescriptions(Description testSuiteDescription, Description testCaseDescription) {
        Description description = new Description();
        if (!StringUtils.isEmpty(testSuiteDescription.getValue())) {

            description.setValue(testSuiteDescription.getValue() + "\n" + testCaseDescription.getValue());
            description.setType(testSuiteDescription.getType().equals(DescriptionType.MARKDOWN) ?
                    DescriptionType.MARKDOWN : testCaseDescription.getType());
        }
        return description;
    }

}
