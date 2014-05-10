package ru.yandex.qatools.allure.storages;

import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.Step;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.12.13
 */
public class StepStorage extends InheritableThreadLocal<Deque<Step>> {
    @Override
    protected Deque<Step> initialValue() {
        Deque<Step> queue = new LinkedList<>();
        queue.add(createRootStep());
        return queue;
    }

    public Step getLast() {
        return get().getLast();
    }

    public void put(Step step) {
        get().add(step);
    }

    public Step pollLast() {
        Deque<Step> queue = get();
        Step last = queue.pollLast();
        if (queue.isEmpty()) {
            queue.add(createRootStep());
        }
        return last;
    }

    public Step createRootStep() {
        return new Step()
                .withName("Root step")
                .withTitle("Allure step processing error: if you see this step something went wrong.")
                .withStart(System.currentTimeMillis())
                .withStatus(Status.BROKEN);
    }
}
