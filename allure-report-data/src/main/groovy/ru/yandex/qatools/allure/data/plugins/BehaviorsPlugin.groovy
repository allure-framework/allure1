package ru.yandex.qatools.allure.data.plugins
import groovy.transform.EqualsAndHashCode
import ru.yandex.qatools.allure.data.AllureBehavior
import ru.yandex.qatools.allure.data.AllureFeature
import ru.yandex.qatools.allure.data.AllureStory
import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.ListWidgetData
import ru.yandex.qatools.allure.data.ReportGenerationException
import ru.yandex.qatools.allure.data.Statistic
import ru.yandex.qatools.allure.data.BehaviorsWidgetItem
import ru.yandex.qatools.allure.data.utils.PluginUtils

import static ru.yandex.qatools.allure.data.utils.TextUtils.generateUid
/**
 * Behaviors plugin add "Behaviors" tab to your report. This tab will
 * sort your test cases by features and stories.
 * (see http://en.wikipedia.org/wiki/Behavior-driven_development)
 *
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.15
 */
@Plugin.Name("behaviors")
@Plugin.Priority(400)
class BehaviorsPlugin extends DefaultTabPlugin implements WithWidget {

    public static final int FEATURES_IN_WIDGET = 10

    @Plugin.Data
    def behavior = new AllureBehavior();

    /**
     * Storage for all features. You can find feature by name.
     */
    protected Map<String, AllureFeature> features = new HashMap<>().withDefault {
        key -> new AllureFeature(title: key, statistic: new Statistic())
    };

    /**
     * Storage for all stories. Stories can be found by {@link Key}.
     */
    protected Map<Key, AllureStory> stories = new HashMap<>().withDefault {
        key -> new AllureStory(title: key.story, statistic: new Statistic())
    };

    /**
     * This cache contains set of uids for each key pair (feature + story).
     * An example if you specify one story for test case twice allure
     * shows it in report only once.
     */
    private Map<Key, Set<String>> cache = new HashMap<>().withDefault {
        key -> new HashSet<>()
    }

    /**
     * Process given test case and save its status to {@link #behavior}
     */
    @Override
    void process(AllureTestCase testCase) {
        if (!testCase.status) {
            throw new ReportGenerationException("Test case status should not be null")
        }

        use(PluginUtils) {
            for (def featureValue : testCase.featureValues) {
                if (!features.containsKey(featureValue)) {
                    behavior.features.add(features[featureValue])
                }

                def feature = features[featureValue]

                for (def storyValue : testCase.storyValues) {
                    def key = new Key(story: storyValue, feature: featureValue)
                    if (!stories.containsKey(key)) {
                        feature.stories.add(stories[key])
                    }

                    def story = stories[key]
                    story.uid = generateUid()

                    story.statistic.update(testCase.status)
                    feature.statistic.update(testCase.status)

                    def info = testCase.toInfo()
                    if (!cache.get(key).contains(info.uid)) {
                        cache.get(key).add(info.uid)
                        story.testCases.add(info)
                    }
                }
            }
        }
    }

    /**
     * Creates a widget from {@link #behavior} data. Takes first {@link #FEATURES_IN_WIDGET}
     * features and their statistics.
     */
    @Override
    Object getWidgetData() {
        def features = behavior.features.take(FEATURES_IN_WIDGET)
        def items = features.collect {
            feature -> new BehaviorsWidgetItem(title: feature.title, statistic: feature.statistic)
        }.sort { it.title }
        new ListWidgetData(totalCount: behavior.features.size(), items: items)
    }

    /**
     * Feature - story pair.
     */
    @EqualsAndHashCode
    private static class Key {
        String feature;
        String story;
    }
}
