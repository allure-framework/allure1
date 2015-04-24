package ru.yandex.qatools.allure.data.plugins;

/**
 * Base interface for all plugin which collect some information
 * from allure results.
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 12.02.15
 */
public interface ProcessPlugin<T> extends Plugin<T> {

    /**
     * All results with type <code>T</code> will passed to this method.
     * You can change given object - it will not affect other plugins.
     */
    void process(T data);

}
