package ru.yandex.qatools.allure.data.plugins;

import ru.yandex.qatools.allure.data.DefectsWidgetItem;
import ru.yandex.qatools.allure.data.WidgetType;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 23.04.15
 */
public class DefectsWidget extends DefaultWidget<DefectsWidgetItem> {

    public DefectsWidget(String name) {
        super(WidgetType.DEFECTS, name);
    }
}
