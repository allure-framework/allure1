package ru.yandex.qatools.allure.data.utils;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;

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
     * @see #isBadXmlCharacter(char) by space
     *
     * @throws IOException If an I/O error occurs
     */
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int numChars = super.read(cbuf, off, len);

        for (int i = off; i < off + numChars; i++) {
            if (isBadXmlCharacter(cbuf[i])) {
                cbuf[i] = '\u0020';
            }
        }

        return numChars;
    }

    /**
     * Detect bad xml 1.0 characters
     *
     * @param c to detect
     * @return true if specified character valid, false otherwise
     */
    private boolean isBadXmlCharacter(char c) {
        boolean cDataCharacter = c < '\u0020' && c != '\t' && c != '\r' && c != '\n';
        cDataCharacter |= (c >= '\uD800' && c < '\uE000');
        cDataCharacter |= (c == '\uFFFE' || c == '\uFFFF');
        return cDataCharacter;
    }

}
