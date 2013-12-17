package ru.yandex.qatools.allure.data.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 08.12.13
 */
@SuppressWarnings("unused")
public class TextUtils {

    private static final String ALGORITHM = "MD5";

    private static final String CHARSET = "UTF-8";

    private static final Integer RADIX = 16;

    public static String generateUid(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        md.update(s.getBytes(CHARSET));
        return new BigInteger(1, md.digest()).toString(RADIX);
    }
}
