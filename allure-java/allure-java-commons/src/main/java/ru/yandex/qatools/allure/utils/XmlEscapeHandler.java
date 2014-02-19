package ru.yandex.qatools.allure.utils;

import com.sun.xml.bind.marshaller.CharacterEscapeHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class XmlEscapeHandler implements CharacterEscapeHandler {

    private XmlEscapeHandler() {
    }

    private static final CharacterEscapeHandler instance = new XmlEscapeHandler();

    @Override
    public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
        int limit = start + length;
        boolean cData = false;
        StringWriter buffer = new StringWriter();

        for (int i = start; i < limit; i++) {
            switch (ch[i]) {
                case '&':
                    buffer.append("&amp;");
                    break;
                case '<':
                    buffer.append("&lt;");
                    break;
                case '>':
                    buffer.append("&gt;");
                    break;
                case '\"':
                    if (isAttVal) {
                        buffer.append("&quot;");
                    } else {
                        buffer.append('\"');
                    }
                    break;
                default:
                    if (isCDATA(ch[i])) {
                        writeEntity(ch[i], buffer);
                        cData = true;
                    } else if (ch[i] > '\u007f') {
                        writeEntity(ch[i], buffer);
                    } else {
                        out.write(ch[i]);
                    }
            }
        }
        if (cData) {
            out.write("<![CDATA[");
        }
        out.write(buffer.toString());
        if (cData) {
            out.write("]]>");
        }
    }

    private void writeEntity(char i, Writer out) throws IOException {
        out.write("&#");
        out.write(Integer.toString(i));
        out.write(';');
    }

    private boolean isCDATA(char c) {
        boolean cDataCharacter = (c < '\u0020' && c != '\t' && c != '\r' && c != '\n');
        cDataCharacter |= (c >= '\uD800' && c < '\uE000');
        cDataCharacter |= (c == '\uFFFE' || c == '\uFFFF');
        return cDataCharacter;
    }

    public static CharacterEscapeHandler getInstance() {
        return instance;
    }
}