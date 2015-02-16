package ru.yandex.qatools.allure.data.io;

/**
 * eroshenkoam
 * 02/02/15
 */
public interface Writer<T> {

    int write(T object);
}
