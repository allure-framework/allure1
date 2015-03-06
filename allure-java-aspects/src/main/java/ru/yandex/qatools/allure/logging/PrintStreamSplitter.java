package ru.yandex.qatools.allure.logging;


import ru.yandex.qatools.allure.logging.external.TeeOutputStream;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.Charset;

/**
 * PrintStream writing into two streams.
 * Created by Mihails Volkovs on 2015.03.05.
 */
public class PrintStreamSplitter extends PrintStream {

    private ByteArrayOutputStream content;

    public PrintStreamSplitter(OutputStream branch) {
        this(new ByteArrayOutputStream(), branch);
    }

    public PrintStreamSplitter(ByteArrayOutputStream out, OutputStream branch) {
        super(new TeeOutputStream(out, branch));
        content = out;
    }

    public PrintStreamSplitter(OutputStream out, OutputStream branch) {
        super(new TeeOutputStream(out, branch));
        content = new ByteArrayOutputStream();
    }

    public String toContent() {
        return new String(content.toByteArray(), Charset.forName("utf-8"));
    }
}
