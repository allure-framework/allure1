package ru.yandex.qatools.allure;

import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.03.15
 */
public class BadXmlCharacterFilterReader extends FilterReader {

    /**
     * Creates a new filtered reader.
     *
     * @param file to read.
     */
    public BadXmlCharacterFilterReader(Path file) throws IOException {
        super(Files.newBufferedReader(file, StandardCharsets.UTF_8));
    }

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
     * @see AllureUtils#isBadXmlCharacter(char) by space
     */
    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int numChars = super.read(cbuf, off, len);
        AllureUtils.replaceBadXmlCharactersBySpace(cbuf, off, len);
        return numChars;
    }
}
