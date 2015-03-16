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

    /**
     * Detect bad xml 1.0 characters
     *
     * @param c to detect
     * @return true if specified character valid, false otherwise
     */
    public static boolean isBadXmlCharacter(char c) {
        boolean cDataCharacter = c < '\u0020' && c != '\t' && c != '\r' && c != '\n';
        cDataCharacter |= (c >= '\uD800' && c < '\uE000');
        cDataCharacter |= (c == '\uFFFE' || c == '\uFFFF');
        return cDataCharacter;
    }

    /**
     * Replace bad xml charactes in given array by space
     *
     * @param cbuf buffer to replace in
     * @param off  Offset from which to start reading characters
     * @param len  Number of characters to be replaced
     */
    public static void replaceBadXmlCharactersBySpace(char[] cbuf, int off, int len) {
        for (int i = off; i < off + len; i++) {
            if (isBadXmlCharacter(cbuf[i])) {
                cbuf[i] = '\u0020';
            }
        }
    }

}
