package ru.yandex.qatools.allure.inject.testdata;

import org.junit.Rule;
import org.junit.rules.TestName;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 10.12.13
 */
@SuppressWarnings("unused")
public class SimpleClass {

    @Rule
    private TestName privateTestNameRule;

    @Rule
    public TestName publicTestNameRule;

    @SomeAnnotation
    private void someMethod() {
        //needed to check annotation
    }

}
