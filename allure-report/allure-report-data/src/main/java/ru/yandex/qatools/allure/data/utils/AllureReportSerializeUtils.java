package ru.yandex.qatools.allure.data.utils;

import org.codehaus.jackson.map.ObjectMapper;
import ru.yandex.qatools.allure.data.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 31.10.13
 */
public final class AllureReportSerializeUtils {

    private static final String LIST_FILES_FILE_NAME = "list-files.json";

    private static final String TEST_CASES_PACK_FILE_NAME = "testcases-pack.json";

    private static final String TEST_SUITES_PACK_FILE_NAME = "testsuites-pack.json";

    private static final String TEST_SUITE_FILE_NAME = "-testsuite.json";

    private static final String TEST_CASE_FILE_NAME = "-testcase.json";

    private AllureReportSerializeUtils() {
    }

    public static void writeListFiles(final File directory, ListFiles listFiles) {
        serializeToJson(directory, LIST_FILES_FILE_NAME, listFiles);
    }

    public static void writeTestCasesPack(final File directory, TestCasesPack testCasesPack) {
        serializeToJson(directory, TEST_CASES_PACK_FILE_NAME, testCasesPack);
    }

    public static void writeTestCases(final File directory, List<AllureTestCase> testCases) {
        for (AllureTestCase testCase : testCases) {
            writeTestCase(directory, testCase);
        }
    }

    public static void writeTestCase(final File directory, AllureTestCase testCase) {
        serializeToJson(directory, testCase.getUid() + TEST_CASE_FILE_NAME, testCase);
    }

    public static void writeTestSuitesPack(final File directory, TestSuitesPack testSuitesPack) {
        serializeToJson(directory, TEST_SUITES_PACK_FILE_NAME, testSuitesPack);
    }

    public static void writeTestSuites(final File directory, List<AllureTestSuite> testSuites) {
        for (AllureTestSuite testSuite : testSuites) {
            writeTestSuite(directory, testSuite);
        }
    }

    public static void writeTestSuite(final File directory, AllureTestSuite testSuite) {
        serializeToJson(directory, testSuite.getUid() + TEST_SUITE_FILE_NAME, testSuite);
    }

    public static void serializeToJson(final File directory, String name, Object obj) {
        try {
            new ObjectMapper().writeValue(new File(directory, name), obj);
        } catch (IOException e) {
            throw new ReportGenerationException(e);
        }
    }
}
