package ru.yandex.qatools.allure.logging;

import java.io.PrintStream;

/**
 * Created by Mihails Volkovs on 2015.03.05.
 */
public class StandardOutputSetter {

    private static PrintStream standardOutput = System.out;

    private static PrintStream standardError = System.err;

    public static void set() {

        // 3-rd party friendly
        standardOutput = System.out;
        standardError = System.err;

        System.setOut(new PrintStreamSplitter(standardOutput, new PrintStream(TestStepLogs.INSTANCE)));
        System.setErr(new PrintStreamSplitter(standardError, new PrintStream(TestStepLogs.INSTANCE)));
    }

    public static void reset() {
        System.setOut(standardOutput);
        System.setErr(standardError);
    }

}
