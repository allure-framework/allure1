package ru.yandex.qatools.allure.experimental.testdata;

import ru.yandex.qatools.allure.experimental.LifecycleListener;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 04.06.14
 */
public class FailingListener extends LifecycleListener {

    public FailingListener() throws Exception {
        throw new Exception("Initialization error");
    }
}
