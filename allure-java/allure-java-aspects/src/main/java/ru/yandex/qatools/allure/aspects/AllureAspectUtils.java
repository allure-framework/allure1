package ru.yandex.qatools.allure.aspects;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;

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
            results[i] = checkForArray(parameters[i]);
        }
        return MessageFormat.format(finalPattern, results);
    }

    private static Object checkForArray(Object obj) {
        if (obj instanceof Object[]) {
            return Arrays.toString((Object[])obj);
        } else {
            if (obj instanceof byte[]) {
                return Arrays.toString((byte[])obj);
            } else {
                if (obj instanceof short[]) {
                    return Arrays.toString((short[])obj);
                } else {
                    if (obj instanceof int[]) {
                        return Arrays.toString((int[])obj);
                    } else {
                        if (obj instanceof long[]) {
                            return Arrays.toString((long[])obj);
                        } else {
                            if (obj instanceof float[]) {
                                return Arrays.toString((float[])obj);
                            } else {
                                if (obj instanceof double[]) {
                                    return Arrays.toString((double[])obj);
                                } else {
                                    if (obj instanceof boolean[]) {
                                        return Arrays.toString((boolean[])obj);
                                    } else {
                                        if (obj instanceof char[]) {
                                            return Arrays.toString((char[])obj);
                                        } else {
                                            return obj;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
