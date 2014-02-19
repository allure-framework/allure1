package ru.yandex.qatools.allure.utils;

import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 19.02.14
 */
public class XmlEscapeHandler implements CharacterEscapeHandler {

    private CharsetEncoder encoder;

    public XmlEscapeHandler(String charset) {
        encoder = Charset.forName(charset).newEncoder();
    }

    @Override
    public void escape(char[] ch, int start, int length, boolean isAttVal, Writer out) throws IOException {
        int limit = start + length;
        boolean cData = false;
        StringWriter buffer = new StringWriter();
        for (int i = start; i < limit; i++) {
            if (isCDATA(ch[i])) {
                writeEntity(ch[i], buffer);
                cData = true;
            } else if (encoder.canEncode(ch[i])) {
                buffer.write(ch[i]);
            } else {
                writeEntity(ch[i], buffer);
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
}