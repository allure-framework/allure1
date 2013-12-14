package ru.yandex.qatools.allure.utils.testdata;

import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Story;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.12.13
 */

public class SimpleClass {

    @Title("some.title")
    @Description("some.description")
    @Severity(SeverityLevel.BLOCKER)
    @Story(Behaviors.MyFeature.MyStory.class)
    public void simpleMethod() {
    }
}
