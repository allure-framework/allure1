package ru.yandex.qatools.allure.utils.testdata;

import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.12.13
 */

public class SimpleClass {

    @Title("some.title")
    @Description("some.description")
    @Severity(SeverityLevel.BLOCKER)
    @Features("some.feature")
    @Stories("some.story")
    public void simpleMethod() {
    }
}
