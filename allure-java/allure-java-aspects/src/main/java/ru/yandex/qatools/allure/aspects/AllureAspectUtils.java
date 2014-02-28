package ru.yandex.qatools.allure.aspects;

import java.text.MessageFormat;
import java.util.Arrays;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.10.13
 */
public final class AllureAspectUtils {
    private AllureAspectUtils() {
    }

    public static String getTitle(String namePattern, String methodName, Object[] parameters) {
        String finalPattern = namePattern.replaceAll("\\{method\\}", methodName);
        Object[] results = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++){
            if (parameters[i] instanceof Object[]){
                results[i] = Arrays.toString((Object[])parameters[i]);
            } else {
                results[i] = parameters[i];
            }
        }
        return MessageFormat.format(finalPattern, results);
    }
}
