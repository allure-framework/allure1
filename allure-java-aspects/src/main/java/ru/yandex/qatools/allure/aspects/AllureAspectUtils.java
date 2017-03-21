package ru.yandex.qatools.allure.aspects;

import ru.yandex.qatools.allure.config.AllureConfig;

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
     * Generate title using name pattern:
     * 1) {i-j}, where i = start index, j = end index.
     * 2) {i+}, where i = start index, '+' means that it's required to loop till the end.
     * 3) All "{method}" substrings will be replaced with given method name.
     * 4) Then replace all "{i}" substrings with i-th parameter.
     */
    public static String getTitle(String namePattern, String methodName, Object instance, Object[] parameters) {
        final int paramsCount = parameters == null ? 0 : parameters.length;
        final String rangePattern = "\\{([0-9])\\+\\}|\\{([0-9])\\-([0-9])\\}";
        final Matcher matcher = Pattern.compile(rangePattern).matcher(namePattern);
        String finalPattern = namePattern;

        while (matcher.find()) {
            final StringBuilder expandedParameters = new StringBuilder();
            // First group is for {i+} pattern, second and third - for {i-j}.
            final int startWith = matcher.group(1) != null ? Integer.parseInt(matcher.group(1)) :
                    (matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 0);
            final int endWith = matcher.group(3) != null ? Integer.parseInt(matcher.group(3)) + 1 : paramsCount;

            // Transform range patterns into sequence.
            for (int i = startWith; i < (endWith < paramsCount ? endWith : paramsCount); i++) {
                expandedParameters.append("{").append(i).append("}").append(",").append(" ");
            }

            if (expandedParameters.length() > 0 && expandedParameters.lastIndexOf(",") >= 0) {
                expandedParameters.deleteCharAt(expandedParameters.lastIndexOf(","));
            }

            // Replace handled pattern with common {i} values to avoid further concatenation.
            finalPattern = finalPattern.replaceFirst(rangePattern, expandedParameters.toString().trim());
        }

        finalPattern = finalPattern
                .replaceAll("\\{method\\}", methodName)
                .replaceAll("\\{this\\}", String.valueOf(instance));

        if (finalPattern.startsWith(",")) {
            finalPattern = finalPattern.replaceFirst(",", "").trim();
        }

        if (finalPattern.endsWith(",")) {
            finalPattern = finalPattern.substring(0, finalPattern.length() - 1).trim();
        }

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
