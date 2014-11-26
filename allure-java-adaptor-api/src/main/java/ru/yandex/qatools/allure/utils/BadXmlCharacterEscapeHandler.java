package ru.yandex.qatools.allure.utils;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

import java.io.IOException;
import java.io.Writer;

/**
 * Doesn't marshall bad XML 1.0 characters.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.12.13
 *         <p/>
 * @see CharacterEscapeHandler
 */
public class BadXmlCharacterEscapeHandler implements CharacterEscapeHandler {

    private static final CharacterEscapeHandler INSTANCE = new BadXmlCharacterEscapeHandler();

    /**
     * Use {@link #getInstance}
     */
    BadXmlCharacterEscapeHandler() {
    }

    /**
     * Implementation of {@link CharacterEscapeHandler#escape(char[], int, int, boolean, java.io.Writer)}
     *
     * @param ch       The array of characters.
     * @param start    The starting position.
     * @param length   The number of characters to use.
     * @param isAttVal true if this is an attribute value literal.
     */
    @Override
    public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
        int limit = start + length;

        for (int i = start; i < limit; i++) {
            switch (ch[i]) {
                case '&':
                    out.write("&amp;");
                    break;
                case '<':
                    out.write("&lt;");
                    break;
                case '>':
                    out.write("&gt;");
                    break;
                case '\"':
                    writeQuotes(out, isAttVal);
                    break;
                default:
                    if (!isBadXmlCharacter(ch[i])) {
                        out.write(ch[i]);
                    }
                    break;
            }
        }
    }

    /**
     * Write quotes to specified writer
     *
     * @param out      writer
     * @param isAttVal true if this is an attribute value literal.
     * @throws IOException if can't write to specified writer
     */
    private void writeQuotes(Writer out, boolean isAttVal) throws IOException {
        if (isAttVal) {
            out.write("&quot;");
        } else {
            out.write('\"');
        }
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

    /**
     * Instance getter
     *
     * @return instance of {@link BadXmlCharacterEscapeHandler}
     */
    public static CharacterEscapeHandler getInstance() {
        return INSTANCE;
    }
}