package ru.yandex.qatools.allure.data.utils;

import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;
import ru.yandex.qatools.allure.config.AllureConfig;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 08.12.13
 */
public final class TextUtils {

    private static final Integer RADIX = 16;
    public static final int UID_RANDOM_BYTES_COUNT = 8;

    private TextUtils() {
    }

    public static String generateUid() {
        SecureRandom rand = new SecureRandom();
        byte[] randomBytes = new byte[UID_RANDOM_BYTES_COUNT];
        rand.nextBytes(randomBytes);
        return new BigInteger(1, randomBytes).toString(RADIX);
    }

    public static String humanize(String text) {
        String result = text.trim();

        Pattern pattern = Pattern.compile("(.*?)(\\[.*\\])");
        Matcher matcher = pattern.matcher(result);

        String params = "";
        if (matcher.matches()) {
            result = matcher.group(1);
            params = ' ' + matcher.group(2); // NOSONAR
        }

        result = simplify(result);
        result = splitCamelCaseWordsWithLowdashes(result);
        result = lowdashesToSpaces(result);
        result = underscoreCapFirstWords(result);
        result = capitalize(result);

        return result + params;
    }

    public static String lowdashesToSpaces(String text) {
        return text.replaceAll("(_)+", " ");
    }

    public static String simplify(String text) {
        return text.replaceAll(".*\\.([^.]+)", "$1");
    }

    public static String capitalize(String text) {
        if (text.isEmpty()) {
            return text;
        }
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

    public static String underscoreCapFirstWords(String text) {
        Matcher matcher = Pattern.compile("(^|\\w|\\s)([A-Z]+)([a-z]+)").matcher(text);
        StringBuilder sb = new StringBuilder();
        int last = 0;
        while (matcher.find()) {
            sb.append(text.substring(last, matcher.start()));
            sb.append(matcher.group(0).toLowerCase());
            last = matcher.end();
        }
        sb.append(text.substring(last));
        return sb.toString();
    }

    public static String splitCamelCaseWordsWithLowdashes(String camelCaseString) {
        return camelCaseString.replaceAll(
                String.format("%s|%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[a-z0-9])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[0-9])",
                        "(?<=[A-Za-z0-9])(?=[\\[])"
                ),
                "_"
        );
    }

    public static String getMessageMask(String message) {
        return message == null ? "" : message.replaceAll("\\s+", " ");
    }
    
    public static String getIssueUrl(String name){
        return String.format(AllureConfig.newInstance().getIssueTrackerPattern(), name);
    }

    public static String getTestUrl(String name){
        return String.format(AllureConfig.newInstance().getTmsPattern(), name);
    }

    public static String processMarkdown(String rawText) {
        return new PegDownProcessor(Extensions.ALL + Extensions.SUPPRESS_ALL_HTML).markdownToHtml(rawText);
    }
}
