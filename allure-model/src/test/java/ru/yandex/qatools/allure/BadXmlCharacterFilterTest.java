package ru.yandex.qatools.allure;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.StringReader;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.05.15
 */
public class BadXmlCharacterFilterTest {

    public static final String PATTERN = "qwerty%sйцукен Ω≈ç√∫˜ß∂ƒ©˙œ∑´†¥%s1234567890&amp;%s";

    public static final String STRING = String.format(PATTERN, "\u0014", "\u0011", "\u0018");

    public static final String ESCAPED = String.format(PATTERN, " ", " ", " ");

    @Test
    public void shouldReadAndEscapeBadXmlCharacters() throws Exception {
        StringReader reader = new StringReader(STRING);
        String escaped = IOUtils.toString(new BadXmlCharacterFilterReader(reader));
        assertEquals(ESCAPED, escaped);
    }

    @Test
    public void shouldWriteAndEscapeBadXmlCharacters() throws Exception {
        StringWriter writer = new StringWriter();
        IOUtils.write(STRING, new BadXmlCharacterFilterWriter(writer));
        assertEquals(ESCAPED, writer.toString());
    }
}
