package ru.yandex.qatools.allure.utils;

import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static ru.yandex.qatools.allure.utils.HashGenerator.sha256Hex;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 30.04.14
 */
public class HashGeneratorTest {

    public static final String FILE1 = "simple-file-attachment.txt";

    public static final String FILE2 = "simple2-file-attachment.txt";

    public static final String FILE3 = "simple3-file-attachment.txt";

    public static final String STRING1 = "simple attachment context";

    public static final String STRING2 = "another attachment context";

    @Test
    public void sha256FileEquals1Test() throws Exception {
        File origin = getResourceAsFile(FILE1);
        File modify = getResourceAsFile(FILE1);
        assertEquals(sha256Hex(origin), sha256Hex(modify));
    }

    @Test
    public void sha256FileEquals2Test() throws Exception {
        File origin = getResourceAsFile(FILE1);
        File modify = getResourceAsFile(FILE2);
        assertEquals(sha256Hex(origin), sha256Hex(modify));
    }

    @Test
    public void sha256FileNotEquals1Test() throws Exception {
        File origin = getResourceAsFile(FILE1);
        File modify = getResourceAsFile(FILE3);
        assertNotEquals(sha256Hex(origin), sha256Hex(modify));
    }

    @Test
    public void sha256FileNotEquals2Test() throws Exception {
        File origin = getResourceAsFile(FILE2);
        File modify = getResourceAsFile(FILE3);
        assertNotEquals(sha256Hex(origin), sha256Hex(modify));
    }

    @Test
    public void sha256StringEquals1Test() throws Exception {
        assertEquals(sha256Hex(STRING1), sha256Hex(STRING1));
    }

    @Test
    public void sha256StringEquals2Test() throws Exception {
        File modify = getResourceAsFile(FILE2);
        assertEquals(sha256Hex(STRING1), sha256Hex(modify));
    }

    @Test
    public void sha256StringNotEqualsTest() throws Exception {
        assertNotEquals(sha256Hex(STRING1), sha256Hex(STRING2));
    }

    public File getResourceAsFile(String resourcePath) throws Exception {
        return new File(getClass().getClassLoader().getResource(resourcePath).toURI());
    }

}
