package ru.yandex.qatools.allure.storages;

import ru.yandex.qatools.allure.model.Step;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.12.13
 */
public class StepStorage extends ThreadLocal<Deque<Step>> {
    @Override
    protected Deque<Step> initialValue() {
        Deque<Step> queue = new LinkedList<>();
        queue.add(new Step());
        return queue;
    }

    public Step getLast() {
        return get().getLast();
    }

    public void put(Step step) {
        get().add(step);
    }

    public Step pollLast() {
        return get().pollLast();
    }
}
