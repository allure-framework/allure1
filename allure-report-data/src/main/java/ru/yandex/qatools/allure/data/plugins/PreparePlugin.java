package ru.yandex.qatools.allure.data.plugins;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 09.02.15
 */
public interface PreparePlugin<T> extends Plugin<T> {

    void prepare(T data);

}
