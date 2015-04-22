package ru.yandex.qatools.allure.data.plugins;

import ru.yandex.qatools.allure.data.StatsWidgetItem;
import ru.yandex.qatools.allure.data.WidgetType;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 23.04.15
 */
public class StatsWidget extends DefaultWidget<StatsWidgetItem> {

    public StatsWidget(String name) {
        super(WidgetType.STATS, name);
    }
}
