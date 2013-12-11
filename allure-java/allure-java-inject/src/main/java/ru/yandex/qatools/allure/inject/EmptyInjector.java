package ru.yandex.qatools.allure.inject;

import org.objectweb.asm.tree.ClassNode;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 25.11.13
 */
public class EmptyInjector extends Injector {
    @Override
    public void inject(ClassNode cn) {
        //empty injector, do nothing
    }

    @Override
    public boolean shouldInject(ClassNode cn) {
        return false;
    }
}
