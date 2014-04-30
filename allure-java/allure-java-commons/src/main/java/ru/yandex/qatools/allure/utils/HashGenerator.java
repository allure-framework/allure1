package ru.yandex.qatools.allure.utils;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 30.04.14
 */
public final class HashGenerator {

    public static final String SHA_256 = "SHA-256";

    private static final String UTF_8 = "UTF-8";

    private static final int HEX = 16;

    private static final int SIGNUM = 1;

    private HashGenerator() {
    }

    public static String sha256Hex(File file) throws NoSuchAlgorithmException, IOException {
        return bytesToHexString(sha256(file));
    }

    public static String sha256Hex(String string) throws NoSuchAlgorithmException, IOException {
        return bytesToHexString(sha256(string));
    }

    public static byte[] sha256(File file) throws IOException {
        return Files.hash(file, Hashing.sha256()).asBytes();
    }

    public static byte[] sha256(String string) throws NoSuchAlgorithmException {
        return sha256(string.getBytes(Charset.forName(UTF_8)));
    }

    public static byte[] sha256(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md = getMessageDigest();
        md.update(bytes);
        return md.digest();
    }

    public static String bytesToHexString(byte[] bytes) {
        return new BigInteger(SIGNUM, bytes).toString(HEX);
    }

    private static MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(SHA_256);
    }
}
