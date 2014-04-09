package ru.yandex.qatools.allure.utils;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

import java.io.IOException;
import java.io.Writer;

/**
 * Doesn't marshall bad XML 1.0 characters.
 */
public class XmlEscapeHandler implements CharacterEscapeHandler {

    private XmlEscapeHandler() {
    }

    private static final CharacterEscapeHandler instance = new XmlEscapeHandler();

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
                    if (isAttVal) {
                        out.write("&quot;");
                    } else {
                        out.write('\"');
                    }
                    break;
                default:
                    if (!isBadXmlCharacter(ch[i])) {
                        out.write(ch[i]);
                    }
            }
        }
    }

    private boolean isBadXmlCharacter(char c) {
        boolean cDataCharacter = (c < '\u0020' && c != '\t' && c != '\r' && c != '\n');
        cDataCharacter |= (c >= '\uD800' && c < '\uE000');
        cDataCharacter |= (c == '\uFFFE' || c == '\uFFFF');
        return cDataCharacter;
    }

    public static CharacterEscapeHandler getInstance() {
        return instance;
    }
}