package ru.yandex.qatools.allure.aspects;

import ru.yandex.qatools.allure.config.AllureConfig;

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.Arrays;

/**
 * Some utils that help process steps and attachments names and titles.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.10.13
 */
public final class AllureAspectUtils {

    /**
     * Don't instance this class
     */
    private AllureAspectUtils() {
    }

    /**
     * Generate method in the following format: {methodName}[{param1}, {param2}, ...]. Cut a generated
     * name is it over {@link ru.yandex.qatools.allure.config.AllureConfig#maxTitleLength}
     */
    public static String getName(String methodName, Object[] parameters) {
        int maxLength = AllureConfig.newInstance().getMaxTitleLength();
        if (methodName.length() > maxLength) {
            return cutBegin(methodName, maxLength);
        } else {
            return methodName + getParametersAsString(parameters, maxLength - methodName.length());
        }
    }

    /**
     * Convert array of given parameters to sting.
     */
    public static String getParametersAsString(Object[] parameters, int maxLength) {
        if (parameters == null || parameters.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < parameters.length; i++) {
            builder.append(arrayToString(parameters[i]));
            if (i < parameters.length - 1) {
                builder.append(", ");
            }
        }
        return cutEnd(builder.toString(), maxLength) + "]";
    }

    /**
     * Generate title using name pattern. First step all "{method}" substrings will be replaced
     * with given method name. Then replace all "{i}" substrings with i-th parameter.
     */
    public static String getTitle(String namePattern, String methodName, Object instance, Object[] parameters) {
        String finalPattern = namePattern
                .replaceAll("\\{method\\}", methodName)
                .replaceAll("\\{this\\}", String.valueOf(instance));
        int paramsCount = parameters == null ? 0 : parameters.length;
        Object[] results = new Object[paramsCount];
        for (int i = 0; i < paramsCount; i++) {
            results[i] = arrayToString(parameters[i]);
        }

        return cutEnd(MessageFormat.format(finalPattern, results), AllureConfig.newInstance().getMaxTitleLength());
    }

    /**
     * {@link Arrays#toString(Object[])} with {@link Arrays#toString(Object[])} for array elements
     */
    public static Object arrayToString(Object obj) {
        if (obj != null && obj.getClass().isArray()) {
            int len = Array.getLength(obj);
            String[] strings = new String[len];
            for (int i = 0; i < len; i++) {
                strings[i] = String.valueOf(Array.get(obj, i));
            }
            return Arrays.toString(strings);
        } else {
            return obj;
        }
    }

    /**
     * Cut all characters from maxLength and replace it with "..."
     */
    public static String cutEnd(String data, int maxLength) {
        if (data.length() > maxLength) {
            return data.substring(0, maxLength) + "...";
        } else {
            return data;
        }
    }

    /**
     * Cut first (length-maxLength) characters and replace it with "..."
     */
    public static String cutBegin(String data, int maxLength) {
        if (data.length() > maxLength) {
            return "..." + data.substring(data.length() - maxLength, data.length());
        } else {
            return data;
        }
    }
}
