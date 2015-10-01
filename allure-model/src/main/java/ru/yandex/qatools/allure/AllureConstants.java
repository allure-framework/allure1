package ru.yandex.qatools.allure;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 01.10.15
 */
public final class AllureConstants {

    /**
     * The name of Allure schema. You can find the schema in resources of allure-model module.
     */
    public static final String SCHEMA_FILE_NAME = "allure.xsd";

    /**
     * The suffix of test suites files. Each test suite file should has such suffix
     * (and .xml extension).
     */
    public static final String TEST_SUITE_FILE_SUFFIX = "-testsuite";

    /**
     * The glob to find test suites in allure results.
     */
    public static final String TEST_SUITE_FILE_GLOB = String.format("*%s.xml", TEST_SUITE_FILE_SUFFIX);

    /**
     * The suffix of attachment files. All attachments should has such suffix.
     */
    public static final String ATTACHMENTS_FILE_SUFFIX = "-attachment";

    /**
     * The glob to find attachments in allure results.
     */
    public static final String ATTACHMENTS_FILE_GLOB = String.format("*%s*", ATTACHMENTS_FILE_SUFFIX);

    /**
     * The version of Allure.
     */
    public static final String VERSION = AllureConfig.class.getPackage().getImplementationVersion();

    /**
     * Do not instantiate.
     */
    AllureConstants() {
        throw new IllegalStateException("You should never instantiate it.");
    }
}
