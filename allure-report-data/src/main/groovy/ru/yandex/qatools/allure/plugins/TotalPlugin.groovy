package ru.yandex.qatools.allure.plugins

import ru.yandex.qatools.allure.AllureTestCase
import ru.yandex.qatools.allure.AllureTotal
import ru.yandex.qatools.allure.Statistic
import ru.yandex.qatools.allure.Time
import ru.yandex.qatools.allure.utils.PluginUtils

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 14.08.15
 */
@Plugin.Name("total")
@Plugin.Priority(700)
class TotalPlugin extends DefaultTabPlugin implements WithWidget {

    @Plugin.Data
    AllureTotal total = new AllureTotal(
            statistic: new Statistic(),
            time: new Time(start: Long.MAX_VALUE, stop: Long.MIN_VALUE)
    )

    @Override
    void process(AllureTestCase testCase) {
        use(PluginUtils) {
            total.statistic.update(testCase.status)
            total.time.update(testCase.time)
        }
    }

    @Override
    Object getWidgetData() {
        total
    }
}
