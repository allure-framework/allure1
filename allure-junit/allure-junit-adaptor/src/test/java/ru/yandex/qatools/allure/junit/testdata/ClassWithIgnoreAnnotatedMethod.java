package ru.yandex.qatools.allure.junit.testdata;

import org.junit.Ignore;
import org.junit.Test;

/**
 * User: lanwen
 * Date: 04.05.14
 * Time: 2:08
 */
public class ClassWithIgnoreAnnotatedMethod {

    public static final String METHOD_NAME_WITH_IGNORE = "methodWithIgnoreAnnotation";

    @Test
    @Ignore
    public void methodWithIgnoreAnnotation() throws Exception {
    }
}
