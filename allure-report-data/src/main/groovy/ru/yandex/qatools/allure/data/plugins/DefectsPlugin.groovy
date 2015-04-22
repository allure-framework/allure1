package ru.yandex.qatools.allure.data.plugins

import groovy.transform.EqualsAndHashCode
import ru.yandex.qatools.allure.data.AllureDefect
import ru.yandex.qatools.allure.data.AllureDefects
import ru.yandex.qatools.allure.data.AllureTestCase
import ru.yandex.qatools.allure.data.DefectItem
import ru.yandex.qatools.allure.data.utils.PluginUtils
import ru.yandex.qatools.allure.model.Status

import static ru.yandex.qatools.allure.data.utils.TextUtils.generateUid
import static ru.yandex.qatools.allure.data.utils.TextUtils.getMessageMask
import static ru.yandex.qatools.allure.model.Status.BROKEN
import static ru.yandex.qatools.allure.model.Status.FAILED

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 06.02.15
 */
@Plugin.Name("defects")
class DefectsPlugin extends TabPlugin {

    @Plugin.Data
    def defects = new AllureDefects(defectsList: [
            new AllureDefect(title: "Product defects", status: FAILED),
            new AllureDefect(title: "Test defects", status: BROKEN)
    ]);

    private Map<Key, DefectItem> defectItems = new HashMap<>()

    @Override
    void process(AllureTestCase testCase) {
        if (!(testCase.status in [FAILED, BROKEN])) {
            return;
        }
        Key key = new Key(uid: getMessageMask(testCase?.failure?.message), status: testCase.status)
        if (!defectItems.containsKey(key)) {
            DefectItem item = new DefectItem(uid: generateUid(), failure: testCase.failure)
            defectItems.put(key, item);
            defects.defectsList.find { it.status == testCase.status }?.defects?.add(item)
        }

        use(PluginUtils) {
            defectItems[key].testCases.add(testCase.toInfo())
        }
    }

    @EqualsAndHashCode
    class Key {
        Status status;
        String uid;
    }
}
