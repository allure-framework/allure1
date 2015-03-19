package ru.yandex.qatools.allure.storages;

import ru.yandex.qatools.allure.model.Status;
import ru.yandex.qatools.allure.model.Step;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Using to storage information about current step context. How it works:
 * <pre>
 * create new step storage | root() -> null
 * start step1             | step1() -> root() -> null
 * finish step1            | root(step1()) -> null
 * start step2             | step2() -> root(step1()) -> null
 * finish step2            | root(step1() -> step2()) -> null
 * ...
 * </pre>
 * In the end just get all children of root step. It's step three which you can see in report
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 13.12.13
 *         <p/>
 * @see ru.yandex.qatools.allure.Allure
 */
public class StepStorage extends InheritableThreadLocal<Deque<Step>> {

    public static final Object LOCK = new Object();

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
     * In case parent thread spawn thread we need create a new queue
     * for child thread but use the only one root step. In the end all steps will be
     * children of root step, all we need is sync adding steps
     * @param parentValue value from parent thread
     * @return local copy of queue in this thread with parent root as first element
     */
    @Override
    protected Deque<Step> childValue(Deque<Step> parentValue) {
        Deque<Step> queue = new LinkedList<>();
        queue.add(parentValue.getFirst());
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
        synchronized (LOCK) {
            get().add(step);
        }
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
     * Move last step to children of previous step. How it works:
     * <pre>
     * before: step1(...) -> step2(child1 -> ... -> childN) -> step3(...) -> ... -> null
     * after:  step2(child1 -> ... -> childN -> step1(...)) -> step3(...) -> ... -> null
     * </pre>
     * @return ex-last step
     */
    public Step adopt() {
        Step step = pollLast();
        synchronized (LOCK) {
            getLast().getSteps().add(step);
        }
        return step;
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
