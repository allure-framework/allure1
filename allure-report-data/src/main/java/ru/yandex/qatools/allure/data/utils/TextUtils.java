package ru.yandex.qatools.allure.data.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 08.12.13
 */
public final class TextUtils {

    private TextUtils() {
    }

    private static final String ALGORITHM = "MD5";

    private static final String CHARSET = "UTF-8";

    private static final Integer RADIX = 16;

    public static String generateUid(String s) throws Exception {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.update(s.getBytes(CHARSET));
        return new BigInteger(1, md.digest()).toString(RADIX);
    }

    public static String generateUid() {
        SecureRandom rand = new SecureRandom();
        byte[] randomBytes = new byte[8];
        rand.nextBytes(randomBytes);
        return new BigInteger(1, randomBytes).toString(RADIX);
    }

    public static String humanize(String text) {
        String result = text.trim();

        Pattern pattern = Pattern.compile("(.*)(\\[.*\\])");
        Matcher matcher = pattern.matcher(result);

        String params = "";
        if (matcher.matches()) {
            result = matcher.group(1);
            params = ' ' + matcher.group(2);
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

        StringBuffer stringBuffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(stringBuffer, matcher.group().toLowerCase());
        }
        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
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
        return message.replaceAll("\\s+", " ");
    }
}
