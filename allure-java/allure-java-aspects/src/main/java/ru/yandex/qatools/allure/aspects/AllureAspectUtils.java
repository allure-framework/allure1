package ru.yandex.qatools.allure.aspects;

import java.text.MessageFormat;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.10.13
 */
public final class AllureAspectUtils {
    private AllureAspectUtils() {
    }

    public static String getTitle(String namePattern, String methodName, Object[] parameters) {
        String finalPattern = namePattern.replaceAll("\\{method\\}", methodName);
        return MessageFormat.format(finalPattern, parameters);
    }
}
