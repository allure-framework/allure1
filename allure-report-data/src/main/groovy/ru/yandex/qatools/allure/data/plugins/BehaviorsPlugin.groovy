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
class BehaviorsPlugin implements ProcessPlugin<AllureTestCase> {

    public static final String BEHAVIORS_JSON = "behaviors.json"

    AllureBehavior behavior = new AllureBehavior();

    private Map<String, AllureFeature> features = new HashMap<>().withDefault {
        key -> new AllureFeature(title: key, statistic: new Statistic());
    };

    private Map<Key, AllureStory> stories = new HashMap<>().withDefault {
        key -> new AllureStory(title: key.story, statistic: new Statistic());
    };

    private Map<Key, Set<String>> cache = new HashMap<>().withDefault {
        key -> new HashSet<>()
    }

    @Override
    void process(AllureTestCase testCase) {
        if (!testCase.status) {
            throw new ReportGenerationException("Test case status should not be null")
        }

        use(PluginUtils) {
            for (def featureValue : testCase.featureValues) {
                if (!features.containsKey(featureValue)) {
                    behavior.features.add(features[featureValue]);
                }

                def feature = features[featureValue]

                for (def storyValue : testCase.storyValues) {
                    def key = new Key(story: storyValue, feature: featureValue)
                    if (!stories.containsKey(key)) {
                        feature.stories.add(stories[key]);
                    }

                    def story = stories[key]
                    story.uid = generateUid();

                    story.statistic.update(testCase.status);
                    feature.statistic.update(testCase.status);

                    def info = testCase.toInfo()
                    if (!cache.get(key).contains(info.uid)) {
                        cache.get(key).add(info.uid)
                        story.testCases.add(info);
                    }
                }
            }
        }
    }

    @Override
    List<PluginData> getPluginData() {
        return Arrays.asList(new PluginData(BEHAVIORS_JSON, behavior));
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
