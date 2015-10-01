package ru.yandex.qatools.allure;

import java.io.FilterWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static ru.yandex.qatools.allure.AllureUtils.replaceBadXmlCharactersBySpace;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.03.15
 */
public class BadXmlCharacterFilterWriter extends FilterWriter {

    /**
     * Create a new filtered writer
     */
    public BadXmlCharacterFilterWriter(Path out) throws IOException {
        this(Files.newBufferedWriter(out, StandardCharsets.UTF_8));
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
     * {@inheritDoc}
     */
    @Override
    public void write(int c) throws IOException {
        write(new char[]{(char) c}, 0, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(String str, int off, int len) throws IOException {
        write(str.toCharArray(), off, len);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        replaceBadXmlCharactersBySpace(cbuf, off, len);
        super.write(cbuf, off, len);
    }
}
