package ru.yandex.qatools.allure.inject;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 23.11.13
 */
public class MyClassLoader extends ClassLoader {

    public MyClassLoader(ClassLoader classLoader) {
        super(classLoader);
    }

    public Class<?> defineClass(String name, byte[] data) {
        return this.defineClass(name, data, 0, data.length);
    }
}


