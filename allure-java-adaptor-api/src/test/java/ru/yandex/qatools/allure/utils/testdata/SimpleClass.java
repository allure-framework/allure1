package ru.yandex.qatools.allure.utils.testdata;

import ru.yandex.qatools.allure.annotations.*;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.12.13
 */

@Title("default.title")
@Description("default.description")
@Features("default.feature")
@Story("default.story")
@Issue("default.issue")
public class SimpleClass {

    @Title("some.title")
    @Description("some.description")
    @Severity(SeverityLevel.BLOCKER)
    @Features("some.feature")
	@Story("some.simple.story")
    @Stories({
        @Story("some.nested.story.1"),
        @Story("some.nested.story.2"),
    })
    @Issue("some.simple.issue")
    @Issues({
        @Issue("some.nested.issue.1"),
        @Issue("some.nested.issue.2")
    })
    @TestCaseId("test.case.id")
    public void simpleMethod() {
    }

    public void defaultMethod(){
    }

    @Severity(SeverityLevel.CRITICAL)
    @Issue("initial.issue")
    public void combinedMethod(){
    }
}
