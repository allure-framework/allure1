package ru.yandex.qatools.allure.commons;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

import static ru.yandex.qatools.allure.config.AllureNamingUtils.replaceBadXmlCharactersBySpace;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.03.15
 */
public class BadXmlCharacterFilterReader extends FilterReader {

    /**
     * Creates a new filtered reader.
     *
     * @param in a Reader object providing the underlying stream.
     * @throws NullPointerException if <code>in</code> is <code>null</code>
     */
    public BadXmlCharacterFilterReader(Reader in) {
        super(in);
    }

    /**
     * Reads characters into a portion of an array, then replace invalid XML characters
     *
     * @throws IOException If an I/O error occurs
     * @see ru.yandex.qatools.allure.config.AllureNamingUtils#isBadXmlCharacter(char) by space
     */
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int numChars = super.read(cbuf, off, len);
        replaceBadXmlCharactersBySpace(cbuf, off, len);
        return numChars;
    }
}
