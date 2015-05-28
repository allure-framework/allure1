package ru.yandex.qatools.allure.commons;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.StringReader;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.05.15
 */
public class BadXmlCharacterFilterReaderTest {

    public static final String PATTERN = "qwerty%sйцукен Ω≈ç√∫˜ß∂ƒ©˙œ∑´†¥%s1234567890&amp;%s";

    @Test
    public void shouldEscapeBadXmlCharacters() throws Exception {
        StringReader reader = new StringReader(String.format(PATTERN, "\u0014", "\u0011", "\u0018"));
        String escaped = IOUtils.toString(new BadXmlCharacterFilterReader(reader));

        assertEquals(escaped, String.format(PATTERN, " ", " ", " "));
    }
}
