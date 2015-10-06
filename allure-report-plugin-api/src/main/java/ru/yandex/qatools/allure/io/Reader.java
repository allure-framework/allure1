package ru.yandex.qatools.allure.io;

import java.util.Iterator;

/**
 * eroshenkoam
 * 02/02/15
 */
public interface Reader<T> extends Iterable<T> {

    @Override
    Iterator<T> iterator();
}
