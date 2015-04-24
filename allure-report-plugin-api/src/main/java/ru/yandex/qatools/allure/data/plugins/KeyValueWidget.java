package ru.yandex.qatools.allure.data.plugins;

import ru.yandex.qatools.allure.data.KeyValueWidgetItem;
import ru.yandex.qatools.allure.data.WidgetType;

/**
 * @author Dmitry Baev charlie@yandex-team.ru
 *         Date: 24.04.15
 */
public class KeyValueWidget extends DefaultWidget<KeyValueWidgetItem> {

    public KeyValueWidget(String name) {
        super(WidgetType.KEY_VALUE, name);
    }
}
