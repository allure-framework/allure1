package ru.yandex.qatools.allure.inject;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 23.11.13
 */
public class ClassLoaderWithPublicDefine extends ClassLoader {

    public ClassLoaderWithPublicDefine(ClassLoader classLoader) {
        super(classLoader);
    }

    public Class<?> defineClass(String name, byte[] data) {
        return this.defineClass(name, data, 0, data.length);
    }
}


