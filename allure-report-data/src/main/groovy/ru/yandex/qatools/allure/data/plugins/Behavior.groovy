package ru.yandex.qatools.allure.data.plugins

import groovy.transform.EqualsAndHashCode
import ru.yandex.qatools.allure.data.AllureBehavior
import ru.yandex.qatools.allure.data.AllureFeature
import ru.yandex.qatools.allure.data.AllureStory
import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.ReportGenerationException
import ru.yandex.qatools.allure.data.Statistic
import ru.yandex.qatools.allure.data.utils.PluginUtils

import static ru.yandex.qatools.allure.data.utils.TextUtils.generateUid

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.15
 */
class Behavior implements ProcessPlugin<AllureTestCase> {

    AllureBehavior behavior = new AllureBehavior();

    private Map<String, AllureFeature> features = new HashMap<>().withDefault {
        key -> new AllureFeature(title: key, statistic: new Statistic());
    };

    private Map<Key, AllureStory> stories = new HashMap<>().withDefault {
        key -> new AllureStory(title: key.story, statistic: new Statistic());
    };

    @Override
    void process(AllureTestCase testCase) {
        if (!testCase.status) {
            throw new ReportGenerationException("Test case status should not be null")
        }

        use(PluginUtils) {
            for (def featureValue : testCase.getFeatureValues()) {
                if (!features.containsKey(featureValue)) {
                    behavior.features.add(features[featureValue]);
                }

                def feature = features[featureValue]

                for (def storyValue : testCase.getStoryValues()) {
                    def key = new Key(story: storyValue, feature: featureValue)
                    if (!stories.containsKey(key)) {
                        feature.stories.add(stories[key]);
                    }

                    def story = stories[key]
                    story.uid = generateUid();

                    story.statistic.update(testCase.status);
                    feature.statistic.update(testCase.status);

                    use(PluginUtils) {
                        story.testCases.add(testCase.toInfo());
                    }
                }
            }
        }
    }

    @Override
    List<PluginData> getPluginData() {
        return Arrays.asList(new PluginData("behavior.json", behavior));
    }

    @Override
    Class<AllureTestCase> getType() {
        return AllureTestCase;
    }

    @EqualsAndHashCode
    class Key {
        String feature;
        String story;
    }
}
