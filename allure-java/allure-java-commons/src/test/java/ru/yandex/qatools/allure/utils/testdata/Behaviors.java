package ru.yandex.qatools.allure.utils.testdata;

import ru.yandex.qatools.allure.annotations.FeatureClass;
import ru.yandex.qatools.allure.annotations.StoryClass;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.12.13
 */
public class Behaviors {
    @FeatureClass("some.feature")
    public class MyFeature {
        @StoryClass("some.story")
        public class MyStory {
        }
    }
}
