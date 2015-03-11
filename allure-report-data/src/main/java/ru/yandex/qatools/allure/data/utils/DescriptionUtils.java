package ru.yandex.qatools.allure.data.utils;

import org.apache.commons.io.FileUtils;
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

        if (testSuiteDescription == null && testCaseDescription == null) {
            return null;
        }

        if (testSuiteDescription == null) {
            return testCaseDescription;
        }

        if (testCaseDescription == null) {
            return testSuiteDescription;
        }

        Description description = new Description();
        if (!StringUtils.isEmpty(testSuiteDescription.getValue())) {

            DescriptionType descriptionType = testSuiteDescription.getType().equals(DescriptionType.MARKDOWN) ?
                    DescriptionType.MARKDOWN : testCaseDescription.getType();

            String descriptionValue = descriptionType.equals(DescriptionType.MARKDOWN) ?
                    String.format("%s\n\n%s", testSuiteDescription.getValue(), testCaseDescription.getValue()) :
                    String.format("%s\n%s", testSuiteDescription.getValue(), testCaseDescription.getValue());

            description.setValue(descriptionValue);
            description.setType(descriptionType);
        }
        return description;
    }

}
