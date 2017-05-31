package ru.yandex.qatools.allure.aspects;

import ru.yandex.qatools.allure.config.AllureConfig;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
     * with given method name. {this} will be replaced with full class name.
     * {class} with short class name. If your object has variable named "allure"
     * then all {"field.name"} will be replaced with its value
     * Then replace all "{i}" substrings with i-th parameter.
     * Example:
     * class: public AllureTestClass
     * { @Step("Invoke method {method} in class {class} ({this}) with args: ({0}, {1})")
     * public void print(Strint s, int n) {}
     * }
     * Invoke method: new AllureTestClass().print("Test", 1);
     * Then step title will be  be: "Invoke method print in class AllureTestClass (ru.yandex.qatools.allure.AllureTestClass$12312) with args: (Test, 1)"
     *
     * E.g. you have class: public AllureParams { public String name = "Allure Name"; private int number = 2}
     * and variable "private AllureParams allure;"
     * So you can use this template: "Test {name} with {num}"
     * Result will be "Test Allure Name with 2"
     */
    public static String getTitle(String namePattern, String methodName, Object instance, Object[] parameters) {
        String finalPattern = namePattern
                .replaceAll("\\{method}", methodName)
                .replaceAll("\\{this}", String.valueOf(instance))
                .replaceAll("\\{class}", instance.getClass().getSimpleName());
        HashMap<String, String> allureParams = getAllureParams(instance);
        finalPattern = replaceParams(allureParams, finalPattern);
        int paramsCount = parameters == null ? 0 : parameters.length;
        Object[] results = new Object[paramsCount];
        for (int i = 0; i < paramsCount; i++) {
            results[i] = arrayToString(parameters[i]);
        }

        return cutEnd(MessageFormat.format(finalPattern, results), AllureConfig.newInstance().getMaxTitleLength());
    }
    
    private static String replaceParams(Map<String, String> hashMap, String template) {
        for (Map.Entry<String, String> e : hashMap.entrySet())
            template = template.replaceAll("\\{" + e.getKey() + "}", e.getValue());
        return template;
    }

    private static HashMap<String, String> getAllureParams(Object instance) {
        try {
            Field f = instance.getClass().getDeclaredField("allure"); //NoSuchFieldException
            f.setAccessible(true);
            Object allure = f.get(instance);
            if (allure != null) {
                if (f.getType().isAssignableFrom(HashMap.class))
                    return (HashMap<String, String>) allure;
                HashMap<String, String> allureMap = new HashMap<>();
                for (Field field : allure.getClass().getFields()) {
                    field.setAccessible(true);
                    allureMap.put(field.getName(), field.get(instance).toString());
                }
                return allureMap;
            }
        } catch (NoSuchFieldException | IllegalAccessException ignore) { }
        return null;
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
