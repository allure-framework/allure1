package ru.yandex.qatools.allure.config;

import java.util.UUID;

/**
 * @author Artem Eroshenko eroshenkoam@yandex-team.ru
 *         Date: 12/24/13
 */
public final class AllureNamingUtils {

    public static final String FILE_NAME_PATTERN = "%s-%s.%s";

    /**
     * Don't create instance
     */
    AllureNamingUtils() {
        throw new IllegalStateException("Don't instance AllureNamingUtils");
    }

    /**
     * Generate suite file name with specified name
     *
     * @param name specified name
     * @return file name \"{name}-testsuite.xml\"
     */
    public static String generateTestSuiteFileName(String name) {
        AllureConfig config = AllureConfig.newInstance();
        return String.format(FILE_NAME_PATTERN,
                name,
                config.getTestSuiteFileSuffix(),
                config.getTestSuiteFileExtension());
    }

    /**
     * Generate suite file name \"{randomUid}-testsutie.xml\"
     *
     * @return test suite file name
     */
    public static String generateTestSuiteFileName() {
        return generateTestSuiteFileName(UUID.randomUUID().toString());
    }

}
