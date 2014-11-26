package ru.yandex.qatools.allure.storages;

import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.Step;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Using to storage information about current step context
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.12.13
 *         <p/>
 * @see ru.yandex.qatools.allure.Allure
 */
public class StepStorage extends InheritableThreadLocal<Deque<Step>> {

    /**
     * Returns the current thread's "initial value". Construct an new
     * {@link java.util.Deque} with root step {@link #createRootStep()}
     *
     * @return the initial value for this thread-local
     */
    @Override
    protected Deque<Step> initialValue() {
        Deque<Step> queue = new LinkedList<>();
        queue.add(createRootStep());
        return queue;
    }

    /**
     * Retrieves, but does not remove, the last element of this deque.
     *
     * @return the tail of this deque
     */
    public Step getLast() {
        return get().getLast();
    }

    /**
     * Inserts the specified element into the queue represented by this deque
     *
     * @param step the element to add
     */
    public void put(Step step) {
        get().add(step);
    }

    /**
     * Removes the last element of deque in the current thread's copy of this
     * thread-local variable. If after this deque is empty add new root step
     * {@link #createRootStep()}
     *
     * @return the element removed from deque
     */
    public Step pollLast() {
        Deque<Step> queue = get();
        Step last = queue.pollLast();
        if (queue.isEmpty()) {
            queue.add(createRootStep());
        }
        return last;
    }

    /**
     * Construct new root step. Used for inspect problems with Allure lifecycle
     *
     * @return new root step marked as broken
     */
    public Step createRootStep() {
        return new Step()
                .withName("Root step")
                .withTitle("Allure step processing error: if you see this step something went wrong.")
                .withStart(System.currentTimeMillis())
                .withStatus(Status.BROKEN);
    }
}
