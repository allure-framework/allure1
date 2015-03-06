package ru.yandex.qatools.allure.logging;

import org.junit.Test;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Created by Mihails Volkovs on 2015.03.06.
 */
public class TestStepLogsTest {

    @Test
    public void singleThread() {
        PrintStream out = new PrintStream(TestStepLogs.INSTANCE);
        out.print("Before...");
        TestStepLogs.addLog();
        out.print("TestStep|");
        TestStepLogs.addLog();
        out.print("NestedTestStep|");
        assertThat(TestStepLogs.removeLog(), is("NestedTestStep|"));
        assertThat(TestStepLogs.removeLog(), is("TestStep|NestedTestStep|"));
        assertThat(TestStepLogs.removeLog(), is(""));
    }

    @Test
    public void parallelExecution() throws InterruptedException {
        final PrintStream out = new PrintStream(TestStepLogs.INSTANCE);
        out.print("Before...");
        TestStepLogs.addLog();
        out.print("Test|");

        Queue<String> logs = new ConcurrentLinkedQueue<String>();
        List<LoggingThread> childThreads = new ArrayList<LoggingThread>();
        final int THREADS_COUNT = 50;
        for (int i = 0; i < THREADS_COUNT; i++) {
            LoggingThread loggingThread = new LoggingThread(out, "" + i, logs);
            childThreads.add(loggingThread);
            loggingThread.start();
        }
        for (Thread thread : childThreads) {
            thread.join();
        }

        // log messages by different threads are not mixed up
        assertThat(TestStepLogs.removeLog(), is("Test|"));
        assertThat(TestStepLogs.removeLog(), is(""));
        assertThat(logs.size(), is(THREADS_COUNT));
        for (int i = 0; i < THREADS_COUNT; i++) {
            assertThat(logs, hasItem("child thread: " + i));
        }
    }

    private static class LoggingThread extends Thread {

        private PrintStream out;

        private Queue<String> logs;

        private LoggingThread(PrintStream out, String name, Queue<String> logs) {
            super(name);
            this.out = out;
            this.logs = logs;
        }

        @Override
        public void run() {
            TestStepLogs.addLog();
            try {
                Thread.sleep(new Random().nextInt(100));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            out.print("child thread: " + getName());
            logs.add(TestStepLogs.removeLog());
        }

    }
}
