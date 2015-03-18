package ru.yandex.qatools.allure.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilterWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

import static ru.yandex.qatools.allure.config.AllureNamingUtils.replaceBadXmlCharactersBySpace;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.03.15
 */
public class BadXmlCharacterFilterWriter extends FilterWriter {

    /**
     * Create a new filtered writer
     */
    public BadXmlCharacterFilterWriter(File out) throws FileNotFoundException {
        this(new FileOutputStream(out));
    }

    /**
     * Create a new filtered writer
     */
    public BadXmlCharacterFilterWriter(OutputStream out) {
        this(new OutputStreamWriter(out, StandardCharsets.UTF_8));
    }

    /**
     * Create a new filtered writer.
     *
     * @param out a Writer object to provide the underlying stream.
     * @throws NullPointerException if <code>out</code> is <code>null</code>
     */
    protected BadXmlCharacterFilterWriter(Writer out) {
        super(out);
    }

    /**
     * Replace invalid XML characters and then write characters
     *
     * @throws IOException If an I/O error occurs
     * @see ru.yandex.qatools.allure.config.AllureNamingUtils#replaceBadXmlCharactersBySpace(char[], int, int)
     */
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        replaceBadXmlCharactersBySpace(cbuf, off, len);
        super.write(cbuf, off, len);
    }
}
