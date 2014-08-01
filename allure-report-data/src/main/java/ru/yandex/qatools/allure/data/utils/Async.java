package ru.yandex.qatools.allure.data.utils;

import ru.yandex.qatools.allure.data.ReportGenerationException;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 07.07.14
 */
public abstract class Async<T> {

    public static final int N_THREADS = 5;

    public void execute(List<T> dataArray) {
        try {
            ExecutorService service = Executors.newFixedThreadPool(N_THREADS);
            for (final T data : dataArray) {
                service.execute(new Runnable() {
                    @Override
                    public void run() {
                        async(data);
                    }
                });
            }
            service.shutdown();
            service.awaitTermination(1, TimeUnit.HOURS);
        } catch (Exception e) {
            throw new ReportGenerationException(e);
        }
    }

    protected abstract void async(T data);
}
