package ru.yandex.qatools.allure.logging;

import ru.yandex.qatools.allure.logging.external.NullOutputStream;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Stack;

/**
 * Created by Mihails Volkovs on 2015.03.05.
 */
public class TestStepLogs extends OutputStream {

    public static final TestStepLogs INSTANCE = new TestStepLogs();

    private static StackThreadLocal printStreams = new StackThreadLocal();

    private TestStepLogs() {
        // hiding constructor
    }

    public static void addLog() {
        PrintStream printStream = getPrintStream();
        printStreams.get().push(new PrintStreamSplitter(printStream));
    }

    public static String removeLog() {
        Stack<PrintStreamSplitter> stack = getStack();
        if (!stack.isEmpty()) {
            PrintStreamSplitter printStream = stack.pop();
            return printStream.toContent();
        }
        return "";
    }

    private static PrintStream getPrintStream() {
        Stack<PrintStreamSplitter> stack = printStreams.get();
        if (stack.isEmpty()) {
            return new PrintStream(new NullOutputStream());
        }
        return stack.peek();
    }

    private static Stack<PrintStreamSplitter> getStack() {
        return printStreams.get();
    }

    @Override
    public void write(int i) throws IOException {
        getPrintStream().write(i);
    }

    private static class StackThreadLocal extends ThreadLocal<Stack<PrintStreamSplitter>> {

        @Override
        protected Stack<PrintStreamSplitter> initialValue() {
            return new Stack<PrintStreamSplitter>();
        }
    }

}